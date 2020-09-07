package main.album;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.drive.DriveDao;
import main.drive.DriveUtils;
import main.img.ImageDataController;
import main.misc.Util;

public class AlbumController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataController.class);
	
	private static Album rootDirectory;
	private static Map<String, Album> albums;
	private DriveDao dao;
	
	public AlbumController(DriveDao dao) {
		this.dao = dao;
		loadAlbums();
	}
	
	public Map<String, Album> getAllAlbums() {
		return albums;
	}
	
	public List<Album> getRootAlbums() {
		return rootDirectory.getAlbums();
	}
	
	public Album getAlbum(String id) throws NullPointerException {
		if(!albums.containsKey(id)) {
			throw new NullPointerException("'"+id+"' album can't be found");
		}
		return albums.get(id);
	}
	
	public void loadAlbums() {
		try {
			List<Album> albumList = dao.getAllFolders();
			albums = new HashMap<>();
			albumList.forEach(s -> {
				if(s.getId().equals(DriveUtils.rootId)) {
					rootDirectory = s;
				}
				albums.put(s.getId(), s);
			});
		} catch(IOException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		} finally {
			if(albums == null) {
				albums = new HashMap<>();
			}
		}
	}
	
}