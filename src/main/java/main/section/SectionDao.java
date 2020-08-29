package main.section;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import main.img.ImageData;

public class SectionDao {
	
	private static String getFoldersUrl;
	private static Type token = 
			new TypeToken<Map<String,List<ImageData>>>() {}.getType();
	
	public SectionDao(String key) {
		getFoldersUrl = "https://www.googleapis.com/drive/v2/files"
				+ "?q=%271zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa%27+in+parents"
				+ "&key="+key
				+ "&mime=application/vnd.google-apps.folder"
				+ "&fields=items(id)";
	}
	
}
