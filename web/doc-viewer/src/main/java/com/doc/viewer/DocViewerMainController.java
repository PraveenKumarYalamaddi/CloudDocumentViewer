package com.doc.viewer;


import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocViewerMainController {

@Autowired
DocViewerService dvs;
	
	@RequestMapping(value = "/getfiledownloadfromfolder/{folderName}/{fileName}")
	public Response getFilesandFolders(@PathVariable String folderName,@PathVariable String fileName) throws Exception{
		Response response = dvs.getFileDownloadFromFolder(folderName,fileName);
		return response;
	}
	
	@RequestMapping(value = "/getfileandfoldersinsidefolder/{folderName}")
		public String getFileAndFoldersInsideFolder(@PathVariable String folderName) {
			String response = dvs.getFilesAndFoldersInAFolder(folderName);
			return response;
		
	}
	
	@RequestMapping(value = "/uploadfiletofolder/{folderName}")
	public String uploadFileToFolder(@PathVariable String folderName) {
		String response = dvs.uploadFilesToFolder(folderName);
		return response;
	}
}
