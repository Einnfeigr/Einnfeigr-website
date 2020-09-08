package main.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import main.exception.RequestException;

public class StandardRequestBuilder implements RequestBuilder {
	
	public Request blank() {
		return new Request();
	}
	
	public Request get() {
		return new Request().method("GET");
	}
	
	public Request get(String address) {
		return get().address(address);
	}
	
	public Request get(String address, String... params) {
		return get(address).parameters(stringsToMap(params));
	}

	public Request post() {
		return new Request().method("POST");
	}

	public Request post(String address) {
		return post().address(address);
	}
	
	public Request post(String address, String... params) {
		return post(address).parameters(stringsToMap(params));
	}
	
	public String performGet(String address) 
			throws IOException, RequestException {
		Response response = get(address).perform();
		if(response.getCode() == 200) {
			return response.getContent();
		} else {
			throw new RequestException(response);
		}
	}
	
	public String performGet(String address, String... params) 
			throws IOException {
		Response response = get(address)
				.parameters(stringsToMap(params))
				.perform();
		if(response.getCode() == 200) {
			return response.getContent();
		} else {
			throw new RequestException(response);
		}
	}
	
	public String performPost(String address) 
			throws IOException, RequestException {
		Response response = post(address).perform();
		if(response.getCode() == 200) {
			return response.getContent();
		} else {
			throw new RequestException(response);
		}
	}
	
	public String performPost(String address, String... params) 
			throws IOException, RequestException {
		Response response = post(address)
				.parameters(stringsToMap(params))
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
