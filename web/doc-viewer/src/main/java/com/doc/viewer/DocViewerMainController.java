package com.doc.viewer;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	
	@RequestMapping(value = "/getfiledownloadfromfolder/{folderName}/{fileName}",method =RequestMethod.GET)
	public @ResponseBody void downloadFile(@PathVariable String folderName ,@PathVariable String fileName,HttpServletResponse response) throws IOException {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		RestTemplate restTemplate = new RestTemplate(messageConverters);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", DocViewerUtil.endPoint("auth"));
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
}
