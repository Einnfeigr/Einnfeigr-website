package main.drive.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.drive.DriveFile;

public abstract class CachedDriveDao<F, D> extends AbstractDriveDao<F, D> {
	
	protected Map<String, DriveFile> fileCache;
	protected Map<String, List<DriveFile>> folderCache;
	
	public CachedDriveDao(String rootId) {
		super(rootId);
		fileCache = new HashMap<>();
		folderCache = new HashMap<>();
	}
	
	public void clearCache() {
		fileCache.clear();
		folderCache.clear();
	}
	
	public void clearCache(String id) {
		if(id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		if(fileCache.containsKey(id)) {
			fileCache.remove(id);
		}
		if(folderCache.containsKey(id)) {
			folderCache.remove(id);
		}
	}
	
	public void clearCache(List<String> ids) {
		if(ids == null || ids.size() == 0) {
			throw new IllegalArgumentException("Id list cannot be null");
		}
		for(String id : ids) {
			clearCache(id);
		}
	}
	
	@Override
	protected List<DriveFile> getDriveFolderContent(String id) 
			throws IOException {
		if(folderCache.containsKey(id)) {
			return folderCache.get(id);
		}
		List<DriveFile> files =  super.getDriveFolderContent(id);
		folderCache.put(id, files);
		return files;
	}
	
	@Override
	protected DriveFile getDriveFile(String id) throws IOException {
		if(fileCache.containsKey(id)) {
			return fileCache.get(id);
		}
		DriveFile file = super.getDriveFile(id);
		fileCache.put(id, file);
		return file;
	}
}
