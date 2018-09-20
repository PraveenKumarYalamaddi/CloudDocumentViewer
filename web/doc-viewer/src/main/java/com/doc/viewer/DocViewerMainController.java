package com.doc.viewer;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DocViewerMainController {

@Autowired
DocViewerService docViewerService;
	
	@RequestMapping()
	@ResponseBody
	public String defalutMethod() {
		StringBuilder sb = new StringBuilder();
		sb.append("Welcome to Document Viewer, Please read API documentaion for usage deatils.");
		return sb.toString();
	}

	@RequestMapping(value = "/getfiledownloadfromfolder/{folderName}/{fileName}",method =RequestMethod.GET)
	public @ResponseBody String downloadFile(@PathVariable String folderName ,@PathVariable String fileName,HttpServletResponse response) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		RestTemplate restTemplate = new RestTemplate(messageConverters);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", DocViewerUtil.endPoint("auth"));
		try {
		String downloadFile = docViewerService.downloadFile(folderName,fileName);
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(downloadFile, HttpMethod.GET,entity,byte[].class);
		if(responseEntity.getStatusCode() == HttpStatus.OK) {
			Path path = Files.write(Paths.get(fileName),responseEntity.getBody()).toAbsolutePath();
			File fi = new File(path.toString());
			InputStream inputStream = new FileInputStream(fi);
			response.setHeader("Content-Disposition", "attachment; filename="+fi.getName());
			IOUtils.copy(inputStream, response.getOutputStream());
	        response.flushBuffer();
	        inputStream.close();
	        return DocViewerUtil.toJson("200 - Requested File Downloaded");
		}
		return DocViewerUtil.toJson("Unkown Error try again");
		}catch(Exception e) {
			return DocViewerUtil.toJson("Unkown Error try again");
		}
	}
	
	@RequestMapping(value = "/getfileandfoldersinsidefolder/{folderName}",method =RequestMethod.GET)
		public String getFileAndFoldersInsideFolder(@PathVariable String folderName) {
			String response = docViewerService.getFilesAndFoldersInAFolder(folderName);
			return response;
		
	}
	
	@RequestMapping(value = "/uploadfiletofolder/{folderName}",method =RequestMethod.GET)
	public String uploadFileToFolder(@PathVariable String folderName) {
		String response = docViewerService.uploadFilesToFolder(folderName);
		return response;
	}
	
	@RequestMapping("*")
	@ResponseBody
	public String fallbackMethod() {
		StringBuilder sb = new StringBuilder();
		sb.append("Oops! Something went wrong with the API , Please read API document for further information");
		return sb.toString();
	}
}
