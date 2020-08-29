package main.drive;

import static main.misc.RequestUtils.generateRequestUrl;
import static main.misc.RequestUtils.performGetRequest;
import static main.misc.RequestUtils.rootId;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import main.img.ImageData;
import main.section.Section;

public class DriveDao {
	
	private static Type token = 
			new TypeToken<Map<String,List<DriveFile>>>() {}.getType();
	
	public List<ImageData> getLatest() throws JsonSyntaxException, IOException {
		String content = performGetRequest(generateRequestUrl(rootId,
				"&orderby=createdTime&pageSize=10&fields=items(id)"));
		Map<String,List<ImageData>> map = new Gson().fromJson(
				content.toString(), token);
		return map.get("items");
	}
	
	public List<ImageData> getAllFiles() throws IOException {
		Map<String, List<DriveFile>> fileMap = getDirectoryContent(rootId);
		return parseFiles(fileMap.get("items"));
	}
	
	public List<Section> getAllFolders() throws IOException {
		Map<String, List<DriveFile>> fileMap = getDirectoryContent(rootId);
		return parseFolders(fileMap.get("items"), new ArrayList<Section>());
	}
	
	private Map<String,List<DriveFile>> getDirectoryContent(String id)
			throws IOException {
		String content = performGetRequest(generateRequestUrl(id,
				"&fields=items(id,mimeType,title,parents(id))"));
		return new Gson().fromJson(content, token);
		
	}
	
	private List<Section> parseFolders(List<DriveFile> files,
			List<Section> sectionList) throws IOException {
		String id;
		for(DriveFile file : files) {
			if(file.getMimeType()
					.equals("application/vnd.google-apps.folder")) {
				id = file.getId();
				Section section = new Section();
				section.setId(id);
				section.setName(file.getTitle());
				section.setImages(parseFiles(getDirectoryContent(id)
						.get("items")));
				sectionList.add(section);
				sectionList.forEach(s -> {
					if(s.getId().equals(file.getParentId())) {
						s.addSection(section);
					}
				});
				parseFolders(getDirectoryContent(id).get("items"), sectionList);		
			} 
		}
		return sectionList;
	}
	
	private List<ImageData> parseFiles(List<DriveFile> files)
			throws IOException {
		List<ImageData> dataList = new ArrayList<>();
		for(DriveFile file : files) {
			if(file.getMimeType()
					.equals("application/vnd.google-apps.folder")) {
				dataList.addAll(parseFiles(getDirectoryContent(
						file.getId()).get("items")));		
			} else {
				ImageData data = new ImageData();
				data.setId(file.getId());
				dataList.add(data);
			}
		}
		return dataList;
	}
}
