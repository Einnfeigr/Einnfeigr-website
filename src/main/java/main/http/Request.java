package main.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

public class Request {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(StandardRequestBuilder.class);
	
	private String method;
	private String address;
	private String content;
	private String contentType;
	private String authorization;
	private Map<String, String> headers;
	private Map<String, String> params;
	
	Request() {}

	public Request method(String method) {
		if(!isMethod(method)) {
			throw new IllegalArgumentException("Given value is not a method");
		}
		this.method = method;
		return this;
	}
	
	private boolean isMethod(String text) {
		boolean matches = false;
		for(RequestMethod requestMethod : RequestMethod.values()) {
			if(requestMethod.name().equals(text)) {
				matches = true;
			}
		}
		return matches;
	}
	
	public Request address(String address) {
		if(!isValidAddress(address)) {
			throw new IllegalArgumentException("Given address is not valid");
		}
		this.address = address;
		return this;
	}

	public boolean isValidAddress(String address) {
		return address.contains("http://") || address.contains("https://") 
				&& address.contains("/");
	}
	
	public Request content(String content) {
		if(params != null && !method.equals("GET")) {
			throw new IllegalStateException(
					"Cannot set both content and params");
		}
		this.content = content;
		return this;
	}

	public Request contentType(String contentType) {
		if(params != null && !method.equals("GET")) {
			throw new IllegalStateException(
					"Cannot set both params and content type");
		}
		this.contentType = contentType;
		return this;
	}

	public Request authorization(String authorization) {
		this.authorization = authorization;
		return this;
	}

	public Request headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public Request parameters(Map<String, String> parameters) { 
		if(content != null && !method.equals("GET")) {
			throw new IllegalStateException(
					"Cannot set both params and content");
		}
		this.params = parameters;
		return this;
	}

	public Response perform() throws IOException {
		String responseContent;
		if(params != null) {
			if(method.equals("GET")) {
				address = appendParams(address, params);
			} else {
				contentType = "application/x-www-form-urlencoded";
			}
		} 
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		if(headers != null) {
			headers.forEach((k, v) -> connection.setRequestProperty(k, v));
		}
		if(authorization != null) {
			connection.setRequestProperty("Authorization", authorization);
		}
		if(content != null) {
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-length", 
					String.valueOf(content.length()));
			connection.setRequestProperty("Content-type", contentType);
			try(BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()))) {
				writer.write(content.toString());
			}
		}
		connection.connect();
		try {
			responseContent = readContent(connection.getInputStream());
		} catch(IOException e) {
			responseContent = readContent(connection.getErrorStream());
		}
		if(logger.isDebugEnabled()) {
			logger.debug("performed "+method+" request on address '"
					+address+"'"
					+" with response code "+connection.getResponseCode());
			logger.debug(responseContent.toString());
		}
		return new Response(connection.getResponseMessage(),
				responseContent, connection.getResponseCode());
	}
	
	private String readContent(InputStream inputStream) throws IOException {
		StringBuilder content = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream))) {
			do {
				content.append(reader.readLine()+"\n");
			} while(reader.ready());
		}
		return content.toString();
	}
	
	private String appendParams(String address, Map<String, String> params) {
		StringBuilder output = new StringBuilder(address);
		if(!address.contains("?")) {
			if(!address.endsWith("/")) {
				output.append("/");
			}
			output.append("?");
		} else {
			if(!address.endsWith("&")) {
				output.append("&");
			}
		}
		boolean first = true;
		for(Entry<String, String> entry : params.entrySet()) {	
			String key = entry.getKey();
			String value = entry.getValue();
			if(first) {
				first = false;
			} else {
				output.append("&");
			}
			output.append(key+"="+value);
		}
		return output.toString();
	}
	
}
