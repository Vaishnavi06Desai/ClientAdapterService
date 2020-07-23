package com.barclays.ospworkflow;

public class JwtResponse {

	private String jwttoken;

	public String getToken() {
		return this.jwttoken;
	}
	public void setToken(String token){
		this.jwttoken = token;
	}
}
