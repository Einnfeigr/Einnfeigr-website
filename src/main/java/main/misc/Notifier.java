package main.misc;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.misc.request.RequestBuilder;
import main.misc.request.StandardRequestBuilder;

public class Notifier {

	private final static Logger logger = 
			LoggerFactory.getLogger(Notifier.class);
	
	public void notifyServer() { 
		try {
			RequestBuilder builder = new StandardRequestBuilder();
			builder.performGetRequest("http://"+InetAddress.getLocalHost().getHostAddress()+":8080");
			logger.info("server has been notified");
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);	
		}
	}
	
}
