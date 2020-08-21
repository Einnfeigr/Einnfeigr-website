package main.img;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import main.misc.Util;

@Component
public class ImageInfoDao {

	private static Type token = new TypeToken<List<ImageData>>() {}.getType();
	private File jsonFile;
	
	public ImageInfoDao() throws IOException {
		try {
			jsonFile = new File(Util.toAbsoluteUrl("imageData.json"));
			if(!jsonFile.exists()) {
				jsonFile.createNewFile();
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			Util.createFile("imageData.json");
			jsonFile = new File(Util.toAbsoluteUrl("imageData.json"));
		}
	}
	
	public List<ImageData> getAll() throws JsonSyntaxException, IOException {
		return new Gson().fromJson(Util.readFile(jsonFile), token);
	}
	
	public void save(List<ImageData> data) throws IOException {
		Util.writeFile(jsonFile, new Gson().toJson(data));
	}
	
}
