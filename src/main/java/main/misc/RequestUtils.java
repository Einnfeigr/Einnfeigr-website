package main.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestUtils {
	
	public static final String rootId = "1zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa";
	private static String key;
	
	public RequestUtils(String key) {
		RequestUtils.key = key;
	}
	
	public static String generateRequestUrl(String id, String params) {
		return "https://www.googleapis.com/drive/v2/files?q=%27"+id
				+"%27+in+parents&key="+key+params;
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
		return content.toString();
    }
}
