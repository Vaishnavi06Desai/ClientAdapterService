package com.barclays.ospworkflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    RestClient client;

    Log log = new Log();

    @PostMapping("/authenticate")
    public ResponseEntity<String> getToken(@RequestBody JwtRequest authenticationRequest){
        log.setLogString("Authenticating user...");
        log.setSender("Client");
        client.Logger(log);
        ResponseEntity re = client.getAuthorizationToken(authenticationRequest);
        return new ResponseEntity<String>(re.getBody().toString(), re.getStatusCode());
    }
}
