package com.doc.viewer;

import java.io.File;
import java.util.Scanner;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DocViewerService {
	
	
	public Response getFileDownloadFromFolder(String folName,String filename) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", DocViewerUtil.endPoint("auth"));
		if(filename == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		File downloadFile;
		if(folName == null) {
			downloadFile = new File((DocViewerUtil.endPoint("file")+filename));
		}else {
			downloadFile = new File((DocViewerUtil.endPoint("file")+folName+"/"+filename));
		}
		ResponseBuilder response = Response.ok((Object) downloadFile);
        response.header("Content-Disposition",("attachment; filename="+filename));
		return response.build();
	}
	
	public String getFilesAndFoldersInAFolder(String folderName) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", DocViewerUtil.endPoint("auth"));
		HttpEntity<?> entity  = new HttpEntity<Object>(headers);
		if(folderName == null) {
			return "400 - Folder Name is Null";
		}
		HttpEntity<String> res = restTemplate.exchange((DocViewerUtil.endPoint("folder_contents")+folderName), HttpMethod.GET,entity,String.class);
		String jsonRes = DocViewerUtil.toJson(res);
		return jsonRes;
	}
	
	public String uploadFilesToFolder(String folderName) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		Scanner s = new Scanner(System.in);
		System.out.println("Please enter the file path to upload a file");
		String filepath = s.nextLine();
		File file = new File(filepath);
		s.close();
		headers.add("Authorization", DocViewerUtil.endPoint("auth"));
		HttpEntity<?> entity  = new HttpEntity<Object>(headers);
		if(folderName == null) {
			return "400 - Folder Name is Null";
		}
		HttpEntity<String> res = restTemplate.exchange((DocViewerUtil.endPoint("folder_contents")+folderName+file), HttpMethod.POST,entity,String.class);
		String jsonRes = DocViewerUtil.toJson(res);
		return jsonRes;
	}

}
