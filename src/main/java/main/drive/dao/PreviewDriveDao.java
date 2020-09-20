package main.drive.dao;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.LoggerFactory;

import main.drive.DriveFile;
import main.drive.DriveFileConverter;
import main.drive.DriveUtils;
import main.http.Request;
import main.http.RequestBuilder;
import main.img.ImageData;
import main.img.preview.PreviewSize;
import main.misc.Util;

public class PreviewDriveDao extends CachedDriveDao<ImageData, List<ImageData>> 
		implements WritableDriveDao<ImageData, BufferedImage> {

	private Map<PreviewSize, String> sizeFolders;
	private static final String META_FILE_ID = "";
	private String meta;
	
	private PreviewDriveDao(DriveUtils driveUtils) {
		super("11JiCs--a_HlVPJhyZB8ZuV9NBcS-CqDe");
		setLogger(LoggerFactory.getLogger(PreviewDriveDao.class));
		setDriveUtils(driveUtils);
		setRequestBuilder(RequestBuilder.getInstance());
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
		loadMeta();
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
	
	@Override
	public void writeFile(ImageData file, BufferedImage image) 
			throws IOException {
		StringBuilder content = new StringBuilder();
		String url = DriveUtils.getUploadUrl();
		String extension = Util.getExtension(file.getTitle());
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
			ImageIO.write(image, extension, ios);
			try(BufferedReader br = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(baos.toByteArray())))) {
				br.lines().forEach(l -> content.append(l+"\n"));
			}
	    }
		Request request = RequestBuilder.getInstance()
				.post(url)
				.contentType("image/"+extension)
				.build();
		request.perform();
	}
	
	private void loadMeta() {
		try {
			meta = RequestBuilder.performGet(DriveUtils.getServerDownloadUrl(
					META_FILE_ID));
		} catch (IOException e) {
			getLogger().error(Util.EXCEPTION_LOG_MESSAGE, e);
		}
	}
	
	private Map<String, String> getMeta(PreviewSize size) {
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
