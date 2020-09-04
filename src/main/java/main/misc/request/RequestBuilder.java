package main.misc.request;

import java.io.IOException;

public interface RequestBuilder {

	public String performGetRequest(String address) throws IOException;
	public String performGetRequest(String address, String params) 
			throws IOException;
	
	public String performPostRequest(String address) throws IOException;
	public String performPostRequest(String address, String params) 
			throws IOException;
	
	public String performRequest(String address, String method) 
			throws IOException;
	public String performRequest(String address, String method, String params)
			throws IOException;
	
}
