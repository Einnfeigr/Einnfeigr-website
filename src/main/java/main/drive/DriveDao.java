package main.drive;


import static main.misc.RequestUtils.generateRequestUrl;
import static main.misc.RequestUtils.performGetRequest;
import static main.misc.RequestUtils.rootId;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.img.ImageData;
import main.section.Section;

public class DriveDao {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(DriveDao.class);
	
	private static Type token = 
			new TypeToken<Map<String,List<DriveFile>>>() {}.getType();
	
	public List<ImageData> getLatest() throws  IOException {
		String content = performGetRequest(generateRequestUrl(
				DriveMethods.FILE_LIST, rootId,
				"&orderby=createdTime&pageSize=10&fields=items(id)"));
		Map<String,List<ImageData>> map = new Gson().fromJson(
				content.toString(), token);
		return map.get("items");
	}
	
	public List<ImageData> getAllFiles() throws IOException {
		return parseFiles(getDirectoryContent(rootId));
	}
	
	public List<Section> getAllFolders() throws IOException {
		return parseFolders(getFile(rootId),
				new ArrayList<Section>());
	}
	
	private DriveFile getFile(String id) throws IOException {
		String content = performGetRequest(generateRequestUrl(
				DriveMethods.FILE_GET, id,
				"&fields=id,mimeType,title"));
		return new Gson().fromJson(content, DriveFile.class);
	}
	
	private List<DriveFile> getDirectoryContent(String id)
			throws IOException {
		String content = performGetRequest(generateRequestUrl(
				DriveMethods.FILE_LIST, id,
				"&fields=items(id,mimeType,title,parents(id))"));
		Map<String,List<DriveFile>> map =  new Gson().fromJson(content, token);
		List<DriveFile> files = map.get("items");
		return files;
	}
	
	private List<Section> parseFolders(DriveFile file,
			List<Section> sectionList) throws IOException {
		if(!file.isDirectory()) {
			return sectionList;
		}
		Section section = getSection(file);
		sectionList.add(section);
		sectionList.forEach(s -> {
			if(s.getId().equals(file.getParentId())) {
				s.addSection(section);
			}
		});
		List<DriveFile> files = getDirectoryContent(file.getId());
		for(DriveFile cFile : files) {
			if(cFile.isDirectory()) {
				parseFolders(cFile, sectionList);		
			} 
		}
		return sectionList;
	}
	
	private Section getSection(DriveFile file) throws IOException {
		Section section = new Section();
		section.setId(file.getId());
		section.setName(file.getTitle());
		section.setImages(parseFiles(getDirectoryContent(file.getId())));
		return section;
	}
	
	private List<ImageData> parseFiles(List<DriveFile> files)
			throws IOException {
		List<ImageData> dataList = new ArrayList<>();
		for(DriveFile file : files) {
			if(file.isDirectory()) {
				dataList.addAll(parseFiles(getDirectoryContent(file.getId())));		
			} else {
				ImageData data = new ImageData();
				data.setId(file.getId());
				dataList.add(data);
			}
		}
		return dataList;
	}
	
}
