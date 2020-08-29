package main.img;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


public class ImageDataDao {

	private static String getFilesUrl;
	private static Type token = 
			new TypeToken<Map<String,List<ImageData>>>() {}.getType();
	
	public ImageDataDao(String key) {
		getFilesUrl = "https://www.googleapis.com/drive/v2/files"
				+ "?q=%271zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa%27+in+parents"
				+ "&key="+key
				+ "&fields=items(id)"
				+ "&orderby=createdTime";
	}
	
	public List<ImageData> getAll() throws JsonSyntaxException, IOException {
		URL url = new URL(getFilesUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		StringBuilder content = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()))) {
			do {
				content.append((char)br.read());
			} while(br.ready());
		}
		Map<String,List<ImageData>> map = new Gson().fromJson(content.toString(),
				token);
		return map.get("items");
	}
	
}
