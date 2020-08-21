package main.img;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import main.misc.Util;

public class ImageDataDao {

	private static Type token = new TypeToken<List<ImageData>>() {}.getType();
	private File jsonFile;
	private static final String relativePath = "static/img/imageData.json";
	
	public ImageDataDao() throws IOException {
		try {
			jsonFile = new File(Util.toAbsoluteUrl(relativePath));
			if(!jsonFile.exists()) {
				jsonFile.createNewFile();
			}
		} catch(IOException e) {
			e.printStackTrace();
			Util.createFile(relativePath);
			jsonFile = new File(Util.toAbsoluteUrl(relativePath));
		}
	}
	
	public List<ImageData> getAll() throws JsonSyntaxException, IOException {
		return new Gson().fromJson(Util.readFile(jsonFile), token);
	}
	
	public void save(List<ImageData> data) throws IOException {
		Util.writeFile(jsonFile, new Gson().toJson(data));
	}
	
}
