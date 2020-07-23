package com.barclays.ospworkflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileHandlingService {

	@Value("${filename}")
	private String filepath;
	
	@Autowired
	private RestClient client;

	Log log = new Log();

	public ResponseEntity<String> sendFileForOcr(MultipartFile file, String documentType, String token) throws IOException {

		log.setLogString("Encoding file to base64 format.");
		log.setSender("Client");
		client.Logger(log);

		Path fileNameAndPath = Paths.get(filepath,file.getOriginalFilename());
		String base64EncodeFile = encode_image(file);
		FileDetails details = new FileDetails();
		details.setfile(base64EncodeFile);
		details.setdocType(documentType);
		details.setFileName(file.getOriginalFilename());
//		byte [] decode_data = Base64.getDecoder().decode(base64EncodeFile);
//		try {
//			Files.write(fileNameAndPath, decode_data);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}


		log.setLogString("Sending file for OCR....");
		log.setSender("Client");
		client.Logger(log);
		ResponseEntity re = client.getOCR(details, token);
		return new ResponseEntity<String>(re.getBody().toString(), re.getStatusCode());
	}

	private String encode_image(MultipartFile file) throws IOException {
		byte[] data = file.getBytes();
		String image_encoded = Base64.getEncoder().encodeToString(data);
		return image_encoded;
	}

}
