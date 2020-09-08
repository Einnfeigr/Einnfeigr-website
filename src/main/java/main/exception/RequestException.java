package main.exception;

import java.io.IOException;

import main.http.Response;

public class RequestException extends IOException {

	private static final long serialVersionUID = -4925779678631468220L;
	
	private Response response;
	
	public RequestException(Response response) {
		super("caused by "+response.getCode()+" "+response.getResponseMessage()+
				" response status");
		this.response = response;
	}
	
	public Response getResponse() {
		return response;
	}
	
	public String getContent() {
		return response.getContent();
	}
}
