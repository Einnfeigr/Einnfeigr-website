package main.misc.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardRequestBuilder implements RequestBuilder {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(StandardRequestBuilder.class);
	
    public String performGetRequest(String address) throws IOException {
    	return performRequest(address, "GET");
    }
    
    public String performRequest(String address, String method) 
    		throws IOException {
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		connection.connect();
		StringBuilder content = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()))) {
			do {
				content.append((char)br.read());
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
