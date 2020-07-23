package com.barclays.ospworkflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;


@Service
public class RestClient {

	@Autowired
	private RestTemplate template;
	
	@Value("${ocr.property.url}")
	String url;

	Log log = new Log();
	
	public ResponseEntity<String> getOCR(FileDetails details, String authorization) {

		log.setLogString("Posting File details to OCRService....:\tFile: " + details.getFileName() + "\tdocType: " + details.docType);
		log.setSender("Client");
		Logger(log);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", authorization);
		System.out.println(authorization);
		HttpEntity<FileDetails> postforOCR = new HttpEntity<>(details, headers);
		ResponseEntity<String> response;
		try {
		 response = template.postForEntity(url, postforOCR, String.class);
			log.setLogString("Processed Data: " + response.getBody());
			log.setSender("Client");
			Logger(log);
		 return ResponseEntity.ok(response.getBody());
		}catch (HttpStatusCodeException ex) {
			return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
		}
	}

	public ResponseEntity<String> getAuthorizationToken(JwtRequest jwtRequest) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<JwtRequest> getToken = new HttpEntity<>(jwtRequest, headers);
		ResponseEntity<JwtResponse> response;
		try {
				response = template.postForEntity("http://localhost:8082/authenticate", getToken, JwtResponse.class);
			log.setLogString("Fetching JSON Web Token...");
			log.setSender("Client");
			Logger(log);
			return ResponseEntity.ok(response.getBody().getToken());
		} catch(HttpStatusCodeException ex) {
			return new ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
		}

		// TODO Auto-generated method stub
	}

	public ResponseEntity<String> Validate(JwtResponse token) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<JwtResponse> validateToken = new HttpEntity<>(token, headers);
		ResponseEntity<String> response =
				template.postForEntity("http://localhost:8082/validate", validateToken, String.class);
		return ResponseEntity.ok(response.getBody());
		// TODO Auto-generated method stub
		//return response.getBody();
	}

	public void Logger(Log log) {
		// TODO Auto-generated method stub
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Log> logging = new HttpEntity<>(log, headers);
		log.setDate(new Date());
		ResponseEntity<?> response =
				template.postForEntity("http://localhost:8084/logger", logging, null);
	}
	

}
