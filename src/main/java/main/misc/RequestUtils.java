package main.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.drive.DriveMethods;

public class RequestUtils {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(RequestUtils.class);
	
	public static final String rootId = "1zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa";
	private static String key;
	
	public RequestUtils(String key) {
		RequestUtils.key = key;
	}
	
	public static String generateRequestUrl(DriveMethods method, String id, 
			String params) {
		switch(method.ordinal()) {
		case(0):
			return "https://www.googleapis.com/drive/v2/files?q=%27"+id
					+"%27+in+parents&key="+key+params;
		case(1):
			return "https://www.googleapis.com/drive/v2/files/"+id
					+"?key="+key+params;
		default:
			return null;	
		}
		
	}    
	
    public static String performGetRequest(String address) throws IOException {
    	return performRequest(address, "GET");
    }
    
    public static String performRequest(String address, String method) 
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
