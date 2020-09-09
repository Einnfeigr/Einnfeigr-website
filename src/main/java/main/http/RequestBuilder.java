package main.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

import main.exception.RequestException;

public class RequestBuilder {
	
	private Request request;
	
	public void reset() {
		this.request = null;
	}
	
	public RequestBuilder method(String method) {
		if(!isMethod(method)) {
			throw new IllegalArgumentException("Given value is not a method");
		}
		request.setMethod(method);
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
	
	public RequestBuilder address(String address) {
		if(!isValidAddress(address)) {
			throw new IllegalArgumentException("Given address is not valid");
		}
		request.setAddress(address);
		return this;
	}

	public boolean isValidAddress(String address) {
		return address.contains("http://") || address.contains("https://") 
				&& address.contains("/");
	}
	
	public RequestBuilder content(String content) {
		if(request.getParams() != null && !request.getMethod().equals("GET")) {
			throw new IllegalStateException(
					"Cannot set both content and params");
		}
		request.setContent(content);
		return this;
	}

	public RequestBuilder contentType(String contentType) {
		if(request.getParams() != null && !request.getMethod().equals("GET")) {
			throw new IllegalStateException(
					"Cannot set both params and content type");
		}
		request.setContentType(contentType);
		return this;
	}

	public RequestBuilder authorization(String authorization) {
		request.getAuthorization();
		return this;
	}

	public RequestBuilder headers(Map<String, String> headers) {
		request.setHeaders(headers);
		return this;
	}

	public RequestBuilder params(Map<String, String> parameters) { 
		if(request.getContent() != null && !request.getMethod().equals("GET")) {
			throw new IllegalStateException(
					"Cannot set both params and content");
		}
		request.setParams(parameters);
		return this;
	}
	
	public RequestBuilder blank() {
		if(request != null) {
			throw new IllegalArgumentException(
					"Cannot initialize request twice");
		}
		request = new Request();
		return this;
	}
	
	public RequestBuilder get() {
		return blank().method("GET");
	}
	
	public RequestBuilder get(String address) {
		return get().address(address);
	}
	
	public RequestBuilder get(String address, String... params) {
		return get(address).params(stringsToMap(params));
	}

	public RequestBuilder post() {
		return blank().method("POST");
	}

	public RequestBuilder post(String address) {
		return post().address(address);
	}
	
	public RequestBuilder post(String address, String... params) {
		return post(address).params(stringsToMap(params));
	}
	
	public String performGet(String address) 
			throws IOException, RequestException {
		Response response = get(address).build().perform();
		if(response.getCode() == 200) {
			return response.getContent();
		} else {
			throw new RequestException(response);
		}
	}
	
	public Request build() {
		return request;
	}
	
	public String performGet(String address, String... params) 
			throws IOException {
		Response response = get(address)
				.params(stringsToMap(params))
				.build()
				.perform();
		if(response.getCode() == 200) {
			return response.getContent();
		} else {
			throw new RequestException(response);
		}
	}
	
	public String performPost(String address) 
			throws IOException, RequestException {
		Response response = post(address).build().perform();
		if(response.getCode() == 200) {
			return response.getContent();
		} else {
			throw new RequestException(response);
		}
	}
	
	public String performPost(String address, String... params) 
			throws IOException, RequestException {
		Response response = post(address)
				.params(stringsToMap(params))
				.build()
				.perform();
		if(response.getCode() == 200) {
			return response.getContent();
		} else {
			throw new RequestException(response);
		}
	}

	private Map<String, String> stringsToMap(String[] text) {
		Map<String, String> map = new HashMap<>();
		for(int x = 0; x < text.length; x += 2) {
			map.put(text[x], text[x+1]);
		}
		return map;
	}
	
}
