package main.drive;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.exception.RequestException;
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
	private long expiresIn;

	public TokenBunch() {
		String refreshToken = System.getenv("refreshToken");
		if(refreshToken != null) {
			this.refreshToken = refreshToken;
		}
	}
	
	public String getAccessToken() {
		if(refreshToken == null) {
			exchangeCode();
		} else if(isExpired()) {
			refreshCode();
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
	
	private void setExpiresIn(long expiresIn) {
		this.expiresIn = System.currentTimeMillis()+expiresIn*1000;
	}
	
	public boolean isExpired() {
		return expiresIn < System.currentTimeMillis();
	}
	
	private void refreshCode() {
		Type token = new TypeToken<Map<String, String>>() {}.getType();	
		Request request;
		Map<String, String> jsonEntries;
		Map<String, String> content = new HashMap<>();
		content.put("client_id", "");
		content.put("client_secret", "");
		content.put("grant_type", "refresh_token");
		content.put("refresh_token", refreshToken);
		request = generateOauthRequest(content);
		try {	
			jsonEntries = gson.fromJson(request.perform().getContent(), token);
			accessToken = jsonEntries.get("accessToken");
			refreshToken = jsonEntries.get("refreshToken");
			setExpiresIn(Integer.valueOf(jsonEntries.get("expiresIn")));
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
		String code;
		try {
			code = URLEncoder
					.encode(userCode, StandardCharsets.UTF_8.toString())+"&";
		} catch(UnsupportedEncodingException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			code = userCode;
		}
		content.put("client_id", System.getenv("clientId"));
		content.put("client_secret", System.getenv("clientSecret"));
		content.put("redirect_uri", System.getenv("currentUrl")+"/login");
		content.put("code", userCode);
		content.put("grant_type", "authorization_code");
		request = generateOauthRequest(content);
		logger.info(userCode);
		try {
			String response = request.perform().getContent();
			logger.info(response);
			jsonEntries = gson.fromJson(response, token);
			accessToken = jsonEntries.get("access_token");
			refreshToken = jsonEntries.get("refresh_token");
			setExpiresIn(Integer.valueOf(jsonEntries.get("expires_in")));
			logger.info("Code has been exchanged");
			logger.info("AccessToken: "+accessToken);
			logger.info("RefreshToken: "+refreshToken);
		} catch(RequestException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e.getContent());
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
