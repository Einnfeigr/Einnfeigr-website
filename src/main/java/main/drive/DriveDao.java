package main.drive;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import main.album.Album;
import main.img.ImageData;
import main.img.ImageDataComparator;
import main.misc.request.BufferedRequestBuilder;
import main.misc.request.RequestBuilder;

public class DriveDao {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(DriveDao.class);
	
	private static Type token = 
			new TypeToken<Map<String,List<DriveFile>>>() {}.getType();		

	private DriveUtils driveUtils;
	private static RequestBuilder requestBuilder = 
			new BufferedRequestBuilder();

	private DriveDao(DriveUtils driveUtils) {
		this.driveUtils = driveUtils;
	}
			
	public List<ImageData> getLatest() throws  IOException {
		String content = requestBuilder.performGetRequest(
				driveUtils.generateRequestUrl(
						DriveMethods.FILE_LIST, DriveUtils.rootId,
						"&orderby=createdTime&fields=items(id)"));
		Map<String,List<ImageData>> map = new Gson().fromJson(
				content.toString(), token);
		return map.get("items");
	}
	
	public List<ImageData> getAllFiles() throws IOException {
		return parseFiles(getDirectoryContent(DriveUtils.rootId), true);
	}
	
	public List<Album> getAllFolders() throws IOException {
		List<Album> albums = parseFolders(getFile(DriveUtils.rootId),
				new ArrayList<>());
		return albums;
	}
	
	private DriveFile getFile(String id) throws IOException {
		String content = requestBuilder.performGetRequest(
				driveUtils.generateRequestUrl(
						DriveMethods.FILE_GET, id,"&fields=id,mimeType,title"));
		return new Gson().fromJson(content, DriveFile.class);
	}
	
	private List<DriveFile> getDirectoryContent(String id)
			throws IOException {
		List<DriveFile> files = null;
		String content = requestBuilder.performGetRequest(
				driveUtils.generateRequestUrl(
						DriveMethods.FILE_LIST, id,
				"&fields=items(id,mimeType,title,parents(id))&orderby=name"));
		Map<String,List<DriveFile>> map;
		try {
			map =  new Gson().fromJson(content, token);
			files = map.get("items");
		} catch(JsonSyntaxException e) {
			logger.error("cannot parse json: \n "+content);
			logger.error("exception is: ", e);
		}
		return files;
	}
	
	private List<Album> parseFolders(DriveFile file,
			List<Album> albumList) throws IOException {
		if(!file.isDirectory()) {
			return albumList;
		}
		Album album = generateAlbum(file);
		//TODO refactor
		albumList.forEach(s -> {
			if(logger.isDebugEnabled()) {
				logger.debug("album: "+file.getId()+" | "+file.getTitle());
				logger.debug("parent id: "+file.getParentId());
			}
			if(s.getId().equals(file.getParentId())) {
				s.addAlbum(album);
			}
		});
		albumList.add(album);
		List<DriveFile> files = getDirectoryContent(file.getId());
		for(DriveFile cFile : files) {
			if(cFile.isDirectory()) {
				parseFolders(cFile, albumList);		
			} 
		}
		return albumList;
	}
	
	private Album generateAlbum(DriveFile file) throws IOException {
		if(!file.isDirectory()) {
			throw new IllegalArgumentException("File must be directory | "
					+file.getTitle());
		}
		Album album = new Album();
		album.setId(file.getId());
		album.setName(file.getTitle());
		List<ImageData> imageList = parseFiles(
				getDirectoryContent(file.getId()), false);
		Comparator<ImageData> comparator = new ImageDataComparator();
		imageList.sort(comparator);
		album.setImages(imageList);
		return album;
	}
	
	private List<ImageData> parseFiles(List<DriveFile> files, boolean recursive)
			throws IOException {
		List<ImageData> dataList = new ArrayList<>();
		for(DriveFile file : files) {
			if(!file.isDirectory()) {
				ImageData data = new ImageData();
				data.setId(file.getId());
				data.setTitle(file.getTitle());
				dataList.add(data);
			} else if(recursive) {
				dataList.addAll(parseFiles(getDirectoryContent(file.getId()),
						recursive));		
			}
		}
		return dataList;
	}
	
}
