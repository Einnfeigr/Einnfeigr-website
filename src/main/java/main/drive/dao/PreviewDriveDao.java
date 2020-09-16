package main.drive.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.drive.preview.PreviewSize;
import main.img.ImageData;

public class PreviewDriveDao extends CachedDriveDao<ImageData, List<ImageData>> {
	
	private Map<PreviewSize, String> sizes;
	
	private PreviewDriveDao() {
		super("11JiCs--a_HlVPJhyZB8ZuV9NBcS-CqDe");
		sizes = new HashMap<>();
	}
	
	@Override
	public List<ImageData> getAllFiles() throws IOException {
		return null;
	}

	@Override
	public List<List<ImageData>> getAllFolders() throws IOException {
		return null;
	}

	@Override
	public String getRoot() throws IOException {
		return null;
	}

	@Override
	public ImageData getFile(String id) throws IOException {
		return null;
	}

	@Override
	public List<ImageData> getFolderContent(String id) throws IOException {
		return null;
	}
	
	
}
