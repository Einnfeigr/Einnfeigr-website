package main.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.http.RequestBuilder;
import main.http.StandardRequestBuilder;

public class Notifier {

	private final static Logger logger = 
			LoggerFactory.getLogger(Notifier.class);
	
	public void notifyServer() { 
		try {
			RequestBuilder builder = new StandardRequestBuilder();
			String url = System.getenv("currentUrl");
			if(url != null) {
				builder.performGet(url);
				logger.info("server has been notified");
			} else {
				logger.warn("server cannot be notified because url is null");
			}
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);	
		}
	}
	
}
