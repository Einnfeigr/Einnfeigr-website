package main.misc;

import java.io.IOException;

public interface RequestBuilder {

	public String performGetRequest(String address) throws IOException;
	public String performRequest(String address, String url) throws IOException;
	
}
