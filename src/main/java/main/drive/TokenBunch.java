package main.drive;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.http.Request;
import main.http.RequestBuilder;
import main.misc.Util;

public class TokenBunch {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(TokenBunch.class);
	
	private static final RequestBuilder builder = RequestBuilder.getInstance();
	private static final Gson gson = new Gson();
	private String refreshToken;
	private String accessToken;
	private String authToken;
	private String userCode;
	private TaskScheduler scheduler;
	
	
	public TokenBunch(TaskScheduler scheduler) {
		this.scheduler = scheduler;
		String refreshToken = System.getenv("refreshToken");
		if(refreshToken != null) {
			this.refreshToken = refreshToken;
		}
	}
	
	public String getAccessToken() {
		if(accessToken == null) {
			logger.info("exchanging code");
			exchangeCode();
		}
		logger.info("accessToken: "+accessToken);
		return accessToken;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getUserCode() {
		return userCode;
	}
	
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	private void refreshCode() {
		Type token = new TypeToken<Map<String, String>>() {}.getType();	
		Request request;
		Map<String, String> jsonEntries;
		Map<String, String> content = new HashMap<>();
		content.put("client_id", System.getenv("clientId"));
		content.put("client_secret", System.getenv("clientSecret"));
		content.put("grant_type", "refresh_token");
		content.put("refresh_token", refreshToken);
		request = generateOauthRequest(content);
		try {	
			Long startTime = System.currentTimeMillis();
			String contentString = request.perform().getContent();
			jsonEntries = gson.fromJson(contentString, token);
			logger.info(contentString);
			accessToken = jsonEntries.get("accessToken");
			refreshToken = jsonEntries.get("refreshToken");
			Date date = new Date();
			Long endTime = System.currentTimeMillis();
			Long delay = endTime-startTime;
			Long expirationTime = (Long.valueOf(jsonEntries.get("expiresIn")));
			date.setTime(System.currentTimeMillis()+(expirationTime-delay));
			Runnable task = () -> refreshCode();
			scheduler.schedule(task, date);
			logger.info("code has been refreshed");
			logger.info("AccessToken: "+accessToken);
			logger.info("RefreshToken: "+refreshToken);
		} catch(IOException | NumberFormatException e) {
			logger.info(Util.EXCEPTION_LOG_MESSAGE, e);
		}
	}
	
	private void exchangeCode() {
		Type token = new TypeToken<Map<String, String>>() {}.getType();	
		Map<String, String> jsonEntries;
		Request request;
		Map<String, String> content = new HashMap<>();
		content.put("client_id", System.getenv("clientId"));
		content.put("client_secret", System.getenv("clientSecret"));
		content.put("redirect_uri", System.getenv("currentUrl")+"/login");
		content.put("code", userCode);
		content.put("grant_type", "authorization_code");
		request = generateOauthRequest(content);
		logger.info(userCode);
		try {
			Long startTime = System.currentTimeMillis();
			String response = request.perform().getContent();
			logger.info(response);
			jsonEntries = gson.fromJson(response, token);
			accessToken = jsonEntries.get("access_token");
			refreshToken = jsonEntries.get("refresh_token");
			Date date = new Date();
			Long endTime = System.currentTimeMillis();
			Long delay = endTime-startTime;
			Long expirationTime = (Long.valueOf(jsonEntries.get("expires_in")));
			date.setTime(System.currentTimeMillis()+(expirationTime-delay));
			Runnable task = () -> refreshCode();
			scheduler.schedule(task, date);
			logger.info("Code has been exchanged");
			logger.info("AccessToken: "+accessToken);
			logger.info("RefreshToken: "+refreshToken);
		} catch(IOException | NumberFormatException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
	}
	
	private Request generateOauthRequest(Map<String, String> content) {
		return builder
				.post("https://oauth2.googleapis.com/token")
				.content(gson.toJson(content))
				.build();	
	}
}
