package main.misc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import main.exception.RequestException;
import main.http.Request;
import main.http.RequestBuilder;
import main.http.Response;
import main.img.ImagePreviewController;

public class Util {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImagePreviewController.class);
	public final static String EXCEPTION_LOG_MESSAGE = 
			"Exception has been caught";
    
    public static String toUpperCase(String text) {
    	String separator;
    	StringBuilder sb;
    	if(text.contains("/")) {
    		separator = "/";
    	} else if(text.contains("\\")) {
    		separator = "\\";
    	} else if(text.contains(".")) {
    		separator = ".";
    	} else {
    		 return Character.toUpperCase(text.charAt(0))+text.substring(1);
    	}
    	sb = new StringBuilder("");
    	for(String string : text.split(separator)) {
    		if(string.length() < 1) {
    			continue;
    		}
    		string = Character.toUpperCase(string.charAt(0))
    				+string.substring(1);
    		sb.append(string+separator);
    	}
    	return sb.toString();
    }
    
    public static String getExtension(String name) {
    	String[] spl = name.split("\\.");
    	if(spl.length < 2) {
    		return null;
    	}
    	return spl[spl.length-1];
    }
    
	public static Map<String, String> stringsToMap(String[] text) {
		Map<String, String> map = new HashMap<>();
		for(int x = 0; x < text.length; x += 2) {
			map.put(text[x], text[x+1]);
		}
		return map;
	}
	
    public static void writeConfigVar(String... var) throws IOException {
    	if(var.length%2 > 0) {
    		throw new IllegalArgumentException(
    				"Must be even number of arguments");
    	}
    	Map<String, String> map = stringsToMap(var);
    	Request request = RequestBuilder.getInstance().blank()
    			.method("PATCH")
    			.address("/apps/"+System.getenv("appId")+"/config-vars")
    			.content(new Gson().toJson(map))
    			.build();
    	Response response = request.perform();
    	if(response.getCode() != 200) {
    		throw new RequestException(response);
    	}
    }
    
    
}
