package com.barclays.ospworkflow;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class FileUploadController {
	
	@Autowired
	FileHandlingService service;

	@Autowired
	RestClient client;

	Log log = new Log();

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {
			"multipart/form-data", MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> acceptFileFromUser(@RequestHeader(value = "Authorization") String token, @RequestPart("file")MultipartFile file, @RequestParam("docType")String documentType) throws IOException{
		log.setLogString("Accepting single file..." + documentType);
		log.setSender("Client");
		client.Logger(log);
		ResponseEntity re = service.sendFileForOcr(file, documentType, token);
		return new ResponseEntity<String>(re.getBody().toString(), re.getStatusCode());
		
	}

	
	@RequestMapping(value = "/uploadMultipleFiles", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String[]> acceptMultipleFileFromUser(@RequestHeader(value = "Authorization") String token, @RequestParam("file")MultipartFile[] files, @RequestParam("docType")String[] documentType) throws IOException{
		log.setLogString("Accepting multiple files..." + "\n" + "Sending files for OCR");
		log.setSender("Client");
		client.Logger(log);
		String[] output = new String[files.length];
		for(int  i = 0; i < files.length; i++) {
			ResponseEntity re = service.sendFileForOcr(files[i], documentType[i], token);
			output[i] = re.getBody().toString();
		}
		return new ResponseEntity<String[]>(output, HttpStatus.OK);
	}

}
