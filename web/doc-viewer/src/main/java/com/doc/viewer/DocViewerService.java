package com.doc.viewer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class DocViewerService {
	
	/**
	 * This method will take two parameters and throws IO Exception
	 * 
	 * @param folderName - From which folder user wants to download file
	 * @param filename - Which file user wants to download
	 * @return This is the file path which user wants to download
	 * @throws IOException
	 */
	public String downloadFile(String folderName,String filename) throws IOException {
		String downloadFile;
		if(folderName == null) {
			downloadFile = (DocViewerUtil.endPoint("file")+filename);
		}else {
			downloadFile = (DocViewerUtil.endPoint("file")+folderName+"/"+filename);
		}
		return downloadFile;
		
	}
	
	/***
	 * 
	 * @param folderName - From which folder user wants to get files info
	 * @return This will be the outcome which user is user requested for
	 */
	public String getFilesAndFoldersInAFolder(String folderName	) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", DocViewerUtil.endPoint("auth"));
		HttpEntity<?> entity  = new HttpEntity<Object>(headers);
		if(folderName == null) {
			return "400 - Folder Name is Null";
		}
		HttpEntity<String> res = restTemplate.exchange((DocViewerUtil.endPoint("folder_contents")+folderName), HttpMethod.GET,entity,String.class);
		return DocViewerUtil.toJson(res);
	}
	
	
	/**
	 * This method requires user input like file path which he wants to upload.
	 * @param folderName - On which folder user wants to upload the file
	 * @return A Success or Failure response
	 * 
	 */
	public String uploadFilesToFolder(String folderName) {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> mvmap = new LinkedMultiValueMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", DocViewerUtil.endPoint("auth"));
		Scanner s = new Scanner(System.in);
		System.out.println("Please enter the file path to upload a file");
		File file = new File(s.nextLine());
		FileSystemResource resource = new FileSystemResource(file);
		s.close();
		mvmap.add("file", resource);
		String url = DocViewerUtil.endPoint("file")+folderName+"/"+file.getName();
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(mvmap,headers);
		ResponseEntity<byte[]> responseEntity =restTemplate.exchange(url,HttpMethod.POST,entity,byte[].class);
		if(responseEntity.getStatusCode()==HttpStatus.OK) {
			return DocViewerUtil.toJson("200 - File Uploaded Successfully");
		}
		return DocViewerUtil.toJson("Unknown Error occured Please try again");
	}

}
