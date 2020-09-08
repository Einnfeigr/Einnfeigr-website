package main.http;

import java.io.IOException;

public interface RequestBuilder {

	public Request blank();
	
	public Request get();
	public Request get(String address);
	public Request get(String address, String... params);
	
	public Request post();
	public Request post(String address);
	public Request post(String address, String... params);	
	
	public String performGet(String address) throws IOException;
	public String performGet(String address, String...params)
			throws IOException;
	
	public String performPost(String address) throws IOException;
	public String performPost(String address, String... params) 
			throws IOException;

}
