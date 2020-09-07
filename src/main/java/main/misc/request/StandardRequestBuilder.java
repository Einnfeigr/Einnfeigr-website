package main.misc.request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardRequestBuilder implements RequestBuilder {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(StandardRequestBuilder.class);
	
	public String performGetRequest(String address) throws IOException {
		return performRequest(address, "GET", null);
	}
	
    public String performGetRequest(String address, String params)
    		throws IOException {
    	return performRequest(address, "GET", params);
    }
    
    public String performPostRequest(String address) throws IOException {
    	return performRequest(address, "POST", null);
    }
    
    public String performPostRequest(String address, String params)
    		throws IOException {
    	return performRequest(address, "POST", params);
    }
    
    public String performRequest(String address, String method)
    		throws IOException {
    	return performRequest(address, method, null);
    }
    
    public String performRequest(String address, String method, String params) 
    		throws IOException {
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		if(params != null) {
			try(BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()))) {
				writer.write(params);
			}
		}
		connection.connect();
		StringBuilder content = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()))) {
			do {
				content.append(br.readLine());
			} while(br.ready());
		}
		if(logger.isDebugEnabled()) {
			logger.debug("performed "+method+" request on address '"
					+address+"'");
			logger.debug(content.toString());
		}
		return content.toString();
    }
}
