package main.drive;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import main.album.Album;
import main.exception.RequestException;
import main.http.Request;
import main.http.RequestBuilder;
import main.img.ImageData;
import main.img.ImageDataComparator;
import main.misc.Util;

public class DriveDao {
	
	private final static String DESCRIPTION_FILE_NAME = "description.txt";
	private Map<String, DriveFile> fileCache;
	private Map<String, List<DriveFile>> folderCache;
	private Map<String, DriveFile> descriptions;
	private DriveUtils driveUtils;
	private static RequestBuilder requestBuilder = 
			new RequestBuilder();
	
	private final static Logger logger = 
			LoggerFactory.getLogger(DriveDao.class);
	
	private static Type token = 
			new TypeToken<Map<String,List<DriveFile>>>() {}.getType();		


	private DriveDao(DriveUtils driveUtils) {
		fileCache = new HashMap<>();
		folderCache = new HashMap<>();
		descriptions = new HashMap<>();
		this.driveUtils = driveUtils;
	}

	public void resetCache(String id) {
		if(fileCache.containsKey(id)) {
			fileCache.remove(id);
		} else if(folderCache.containsKey(id)) {
			folderCache.remove(id);
		} else {
			logger.warn("Given file doesn't have cache | "+id);
		}
	}
	
	public void resetCache(List<String> ids) {
		if(ids == null) {
			throw new IllegalArgumentException("Id list is null");
		}
		for(String id: ids) {
			resetCache(id);
		}
	}
	
	public void resetCache() {
		fileCache.clear();
		folderCache.clear();
	}
	
	public List<ImageData> getLatest() throws  IOException {
		String content;
		content = requestBuilder.performGet(driveUtils.generateRequestUrl(
				DriveMethods.FILE_LIST, DriveUtils.rootId),
				"orderby","createdTime","fields","items(id)");
		Map<String,List<ImageData>> map = new Gson().fromJson(
				content.toString(), token);
		return map.get("items");
	}
	
	public List<ImageData> getAllFiles() throws IOException {
		List<ImageData> dataList;
		List<DriveFile> files = getDirectoryContent(DriveUtils.rootId);
		if(files != null) {
			dataList = parseFiles(files, true);
		} else {
			dataList = new ArrayList<>();
		}
		return dataList;
	}
	
	public List<Album> getAllFolders() throws IOException {
		List<Album> albums = new ArrayList<>();
		DriveFile rootFile = getRoot();
		if(rootFile != null) {
			albums = parseFolders(rootFile, albums);
		} else {
			logger.warn("Root file is null!");
		}
		return albums;
	}
	
	private DriveFile getRoot() throws IOException {
		return getFile(DriveUtils.rootId);
	}
	
	private DriveFile getFile(String id) throws IOException {
		if(fileCache.containsKey(id)) {
			return fileCache.get(id);
		}
		DriveFile file = null;
		String url = driveUtils.generateRequestUrl(DriveMethods.FILE_GET, id);
		String content;
		try {
			content = requestBuilder
					.performGet(url, "fields", "id,mimeType,title");
			file = new Gson().fromJson(content, DriveFile.class);
			fileCache.put(file.getId(), file);
		} catch(RequestException e) {
			logger.error(url, e);
			logger.error(e.getResponse().getContent());
		} catch(NullPointerException e) {
			logger.error(url, e);
		}
		return file;
	}
	
	private List<DriveFile> getDirectoryContent(String id)
			throws IOException {
		if(folderCache.containsKey(id)) {
			return folderCache.get(id);
		}
		Map<String,List<DriveFile>> map;
		List<DriveFile> files = null;
		String url = driveUtils.generateRequestUrl(DriveMethods.FILE_LIST, id);
		String content;
		try {
			content = requestBuilder.performGet(url,
					"fields","items(id,mimeType,title,parents(id))",
					"orderby","name");
			try {
				if(content == null) {
					throw new NullPointerException(content);
				}
				map =  new Gson().fromJson(content, token);
				if(map == null) {
					throw new NullPointerException(content);
				}
				files = map.get("items");
				folderCache.put(id, files);
				for(DriveFile file : files) {
					if(file.getTitle().equals(DESCRIPTION_FILE_NAME)) {
						descriptions.put(id, file);
					}
				}
			} catch(JsonSyntaxException e) {
				logger.error("cannot parse json: \n "+content);
				logger.error("exception is: ", e);
			}
		} catch(RequestException e) {
			logger.error(url, e);
			logger.error(e.getResponse().getContent());
		} catch(NullPointerException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			return new ArrayList<DriveFile>();
		}
		return files;
	}
	
	private List<Album> parseFolders(DriveFile file,
			List<Album> albumList) throws IOException {
		if(file == null) {
			throw new IllegalArgumentException();
		}
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
		List<ImageData> imageList = new ArrayList<>();
		parseFiles(getDirectoryContent(file.getId()), false).stream()
				.filter(data -> data.getTitle().contains(".jpg") 
						|| data.getTitle().contains(".jpeg"))
				.forEach(data -> imageList.add(data));
		Comparator<ImageData> comparator = new ImageDataComparator();
		imageList.sort(comparator);
		album.setImages(imageList);
		album.setParent(file.getParentId());
		if(descriptions.containsKey(file.getId())) {
			DriveFile descFile = descriptions.get(file.getId());
			RequestBuilder builder = new RequestBuilder();
			Request request = builder
					.get()
					.address(DriveUtils.getDownloadUrl(descFile.getId()))
					.build();
			album.setDescription(request.perform().getContent());
			
		}
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
