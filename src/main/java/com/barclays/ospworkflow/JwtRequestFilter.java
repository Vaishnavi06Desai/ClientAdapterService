package com.barclays.ospworkflow;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@CrossOrigin(origins = "http://localhost:4200/*")
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private RestClient Client;

	@Value("${username}")
	String username;

	@Value("${password}")
	String password;

	@Autowired
	RestClient client;

	Log log = new Log();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		log.setLogString("Fetching Token from header");
		log.setSender("Client");
		client.Logger(log);

		//String username = null;
		JwtResponse jwtToken = new JwtResponse();
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken.setToken(requestTokenHeader.substring(7));

			log.setLogString("Checking if already authenticated....");
			log.setSender("Client");
			client.Logger(log);

			if (jwtToken.getToken() != null && SecurityContextHolder.getContext().getAuthentication() == null){

				log.setLogString("Not Authenticated. Validating token......");
				log.setSender("Client");
				client.Logger(log);
				//Validate Token
				HttpStatus validate = Client.Validate(jwtToken).getStatusCode();
				if(validate == HttpStatus.OK)
				{
					log.setLogString("Token is valid. Authenticating user....");
					log.setSender("Client");
					client.Logger(log);
					UserDetails userDetails = new User(username, password,
							new ArrayList<>());
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}

				else{
					log.setLogString("Token is invalid." + "\n" + "Error: " + validate);
					log.setSender("Client");
					client.Logger(log);
					//response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
					//System.out.println(validate);
				}
			}
		}
		else {

			log.setLogString("Error: JWT Token not found. Token does not begin with Bearer String. " + requestTokenHeader);
			log.setSender("Client");
			client.Logger(log);
			//logger.warn("JWT Token does not begin with Bearer String");
		}

		chain.doFilter(request, response);
	}

}