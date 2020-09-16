package main.drive.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import main.drive.DriveFile;
import main.drive.preview.PreviewSize;
import main.img.ImageData;
import main.misc.Util;

public class PreviewDriveDao extends CachedDriveDao<ImageData, List<ImageData>> {
	
	private Map<PreviewSize, String> sizeFolders;
	
	private PreviewDriveDao() {
		super("11JiCs--a_HlVPJhyZB8ZuV9NBcS-CqDe");
		setLogger(LoggerFactory.getLogger(PreviewDriveDao.class));
		DriveFileConverter<ImageData> fileConverter = (file) -> {
			ImageData data = new ImageData();
			data.setId(file.getId());
			data.setTitle(file.getTitle());
			return data;
		};
		setFileConverter(fileConverter);
		
		sizeFolders = new HashMap<>();
		List<DriveFile> files;
		try {
			files = getDriveFolderContent(rootId);
			for(PreviewSize size : PreviewSize.values()) {
				for(DriveFile file : files) {
					if(file.getTitle().equalsIgnoreCase(size.toString())) {
						sizeFolders.put(size, file.getId());
					}
				}
			}
		} catch(IOException e) {
			getLogger().error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
	}
	
	@Override
	public List<ImageData> getAllFiles() throws IOException {
		return parseFiles(super.getDriveFolderContent(getRoot()), true);
	}

	@Override
	public List<List<ImageData>> getAllFolders() throws IOException {
		List<List<ImageData>> dataList = new ArrayList<>();
		return parseFolders(getRootFile(), dataList);
	}

	@Override
	public ImageData getFile(String id) throws IOException {
		return null;
	}

	@Override
	public List<ImageData> getFolderContent(String id) throws IOException {
		return null;
	}
	
	public List<ImageData> getAllPreviewsBySize(PreviewSize size) 
			throws IOException {
		List<ImageData> images = null;
		String folderId;
		if((folderId = getSizeFolderId(size)) == null) {
			return images;
		}
		images = getFolderContent(folderId);
		return images;
	}
	
	public String getSizeFolderId(PreviewSize size) {
		return sizeFolders.get(size);
	}
	
}
