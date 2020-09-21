package main.drive.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import main.album.Album;
import main.drive.DriveFile;
import main.drive.DriveFileConverter;
import main.drive.DriveMethods;
import main.drive.DriveUtils;
import main.http.Request;
import main.http.RequestBuilder;
import main.img.ImageData;
import main.img.ImageDataComparator;
import main.misc.Util;

public class PortfolioDriveDao extends CachedDriveDao<ImageData, Album> {
	
	private final static String DESCRIPTION_FILE_NAME = "description.txt";
	private Map<String, DriveFile> descriptions;

	private PortfolioDriveDao(DriveUtils driveUtils) {
		super("1zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa");
		descriptions = new HashMap<>();
		setDriveUtils(driveUtils);
		setRequestBuilder(RequestBuilder.getInstance());
		setLogger(LoggerFactory.getLogger(PortfolioDriveDao.class));
		DriveFileConverter<ImageData> fileConverter = (file) -> {
			ImageData data = new ImageData();
			data.setId(file.getId());
			data.setTitle(file.getTitle());
			return data;
		};
		setFileConverter(fileConverter);
		DriveFileConverter<Album> folderConverter = new DriveFileConverter<Album>() {
			public Album convert(DriveFile file) throws IOException {
				if(!file.isDirectory()) {
					throw new IllegalArgumentException(
							"File must be directory | "+file.getTitle());
				}
				Album album = new Album();
				album.setId(file.getId());
				album.setName(file.getTitle());
				List<ImageData> imageList = new ArrayList<>();
				parseFiles(getDriveFolderContent(file.getId()), false).stream()
						.filter(data -> data.getTitle().contains(".jpg") 
								|| data.getTitle().contains(".jpeg"))
						.forEach(data -> imageList.add(data));
				Comparator<ImageData> comparator = new ImageDataComparator();
				imageList.sort(comparator);
				album.setImages(imageList);
				album.setParent(file.getParentId());
				file.getChildren().forEach(f -> {
					if(!f.isDirectory()) {
						return;
					}
					try {
						album.addAlbum(convert(f));
					} catch (IOException e) {
						getLogger().error(Util.EXCEPTION_LOG_MESSAGE, e);
					}
				});
				if(descriptions.containsKey(file.getId())) {
					DriveFile descFile = descriptions.get(file.getId());
					RequestBuilder builder = RequestBuilder.getInstance();
					Request request = builder
							.get()
							.address(DriveUtils.getClientDownloadUrl(descFile
									.getId()))
							.build();
					album.setDescription(request.perform().getContent());
					
				}
				return album;
			}
		};
		setFolderConverter(folderConverter);
	}
	
	public List<ImageData> getLatest() throws  IOException {
		String content;
		content = RequestBuilder.performGet(getDriveUtils()
				.generateRequestUrl(DriveMethods.FILE_LIST, rootId),
				"orderby","createdTime","fields","items(id)");
		Map<String,List<ImageData>> map = new Gson().fromJson(
				content.toString(), token);
		return map.get("items");
	}
	
	@Override
	public List<ImageData> getAllFiles() throws IOException {
		List<ImageData> dataList;
		List<DriveFile> files = getDriveFolderContent(rootId);
		if(files != null) {
			dataList = parseFiles(files, true);
		} else {
			dataList = new ArrayList<>();
		}
		return dataList;
	}
	
	@Override
	public List<Album> getAllFolders() throws IOException {
		List<Album> albums = new ArrayList<>();
		DriveFile rootFile = getRootFile();
		if(rootFile == null) {
			getLogger().warn("Root file is null!");
			return albums;
		}		
		return parseFolders(rootFile, albums);
	}
	
	@Override
	public ImageData getFile(String id) throws IOException {
		return getFileConverter().convert((getDriveFile(id)));
	}
	
	@Override
	protected List<DriveFile> getDriveFolderContent(String id) 
			throws IOException {
		List<DriveFile> files = super.getDriveFolderContent(id);
		parseDescriptions(files, id);
		return files;
	}
	
	@Override
	public String getRoot() {
		return rootId;
	}

	public static String getRootId() {
		return "1zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa";
	}
	
	public List<ImageData> getFolderContent(String id) throws IOException {
		List<DriveFile> fileList = getDriveFolderContent(id);
		List<ImageData> dataList = new ArrayList<>();
		fileList.forEach(f -> {
			try {
				dataList.add(getFileConverter().convert((f)));
			} catch (IOException e) {
				getLogger().error(Util.EXCEPTION_LOG_MESSAGE, e);
			}
		});
		return dataList;
	}
	
	private void parseDescriptions(List<DriveFile> files, String id) {
		for(DriveFile file : files) {
			if(file.getTitle().equals(DESCRIPTION_FILE_NAME)) {
				descriptions.put(id, file);
			}
		}
	}
}
