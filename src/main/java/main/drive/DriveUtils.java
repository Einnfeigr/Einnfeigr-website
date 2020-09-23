package main.drive;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.http.Request;
import main.http.RequestBuilder;

public class DriveUtils {
	
	@Autowired
	private TaskScheduler scheduler;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DriveUtils.class);
	
	private TokenBunch tokenBunch;
	private static String key;
	private static Map<String, String> channelIds = new HashMap<>();

	public DriveUtils() {
		tokenBunch = new TokenBunch();
		DriveUtils.key = System.getenv("drive.apiKey");
	}
	
	public String getUserCode() {
		return tokenBunch.getUserCode();
	}

	public void setUserCode(String userCode) {
		tokenBunch.setUserCode(userCode);
	}
	
	public String generateRequestUrl(DriveMethods method, String id) {
		switch(method) {
		case FILE_LIST:
			return "https://www.googleapis.com/drive/v2/files?q=%27"+id
					+"%27+in+parents&key="+key;
		case FILE_GET:
			return "https://www.googleapis.com/drive/v2/files/"+id
					+"?key="+key;
		case FILE_INSERT:
			return "https://www.googleapis.com/upload/drive/v2/files";
		default:
			return null;	
		}	
	}   
	
	public String getAccessToken() {
		return tokenBunch.getAccessToken();
	}
	
	public static String getServerDownloadUrl(String id) {
		return "https://www.googleapis.com/drive/v2/files/"+id+"?alt=media";
	}
	
	public static String getClientDownloadUrl(String id) {
		return "https://drive.google.com/uc?id="+id+"&export=download";
	}
	
	public static String getUploadUrl() {
		return "https://www.googleapis.com/upload/drive/v3/files"
				+ "?uploadType=resumable";
	}
	
	public void registerWatchService(String channelId, String fileId)
			throws IOException {
		Long startTime = System.currentTimeMillis();
		RequestBuilder builder = RequestBuilder.getInstance();
		Map<String, String> content = new HashMap<>();
		content.put("id", channelId);
		content.put("type", "webhook");
		content.put("address", System.getenv("currentUrl")
				+"/api/drive/callback");
		Request request = builder.blank()
				.address("https://www.googleapis.com/drive/v3/files/"+
					fileId+"/watch/")
				.method("POST")
				.content(new Gson().toJson(content))
				.authorization("Bearer "+getAccessToken())
				.contentType("application/json")
				.build();
		Type token = new TypeToken<Map<String,String>>() {}.getType();	
		Map<String, String> data;
		String responseContent = request.perform().getContent();
		data = new Gson().fromJson(responseContent, token);
		Long expirationTime = Long.valueOf(data.get("expiration"));
		logger.info(responseContent);
		Runnable task = () -> {
			try {
				registerWatchService(channelId, fileId);
				logger.info("Watch service for file "+fileId+" has been "
						+ "refreshed");
			} catch (IOException e) {
				logger.error("Unable to refresh watch service for file "+fileId);
				logger.error("Exception is: ", e);
			}
		};
		Date date = new Date();
		Long endTime = System.currentTimeMillis();
		Long delay = endTime-startTime;
		date.setTime(System.currentTimeMillis()+(expirationTime-delay));
		scheduler.schedule(task, date);
		channelIds.put(fileId, channelId);
	}
}
