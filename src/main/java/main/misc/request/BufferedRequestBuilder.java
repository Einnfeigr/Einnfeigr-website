package main.misc.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferedRequestBuilder extends StandardRequestBuilder {

	private final static Logger logger = 
			LoggerFactory.getLogger(BufferedRequestBuilder.class);
	
	private static List<RequestData> requestBuffer =
			new ArrayList<>();
    
	private int maxCalls;
	
	public BufferedRequestBuilder(int maxCalls) {
		this.maxCalls = maxCalls;
	}
	
	public BufferedRequestBuilder() {
		maxCalls = -1;
	}
	
	@Override
	public String performGetRequest(String address) throws IOException {
    	return performRequest(address, "GET");
    }
	
	@Override
	public String performRequest(String address, String method) 
    		throws IOException {
		RequestData data;
		int index;
		data = new RequestData(address, method);
		if((index = requestBuffer.indexOf(data)) > -1) {
			data = requestBuffer.get(index);
			if(maxCalls == -1 || data.getCalls() < maxCalls) {
				if(logger.isDebugEnabled()) {
					logger.debug("Returned '"+address+"' from buffer");
				}
				return data.getResponse().getContent();
			} else {
				requestBuffer.remove(index);
			}
		}
		String response = super.performRequest(address, method);
		data.reset();
		data.setResponse(new ResponseData(response));
		requestBuffer.add(data);
		return response;
	}
	
}
