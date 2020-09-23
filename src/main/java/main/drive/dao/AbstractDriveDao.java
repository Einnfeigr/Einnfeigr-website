package main.drive.dao;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import main.drive.DriveFile;
import main.drive.DriveFileConverter;
import main.drive.DriveMethods;
import main.drive.DriveUtils;
import main.exception.RequestException;
import main.http.RequestBuilder;
import main.misc.Util;

public abstract class AbstractDriveDao<F, D> implements DriveDao<F, D> {

	private Logger logger;
	protected final String rootId;
	private DriveUtils driveUtils;
	private RequestBuilder requestBuilder;
	private DriveFileConverter<F> fileConverter;
	private DriveFileConverter<D> folderConverter;	
	protected static Type token = new TypeToken<Map<String,List<DriveFile>>>() 
			{}.getType();	
			
	protected AbstractDriveDao(String rootId) {
		this.rootId = rootId;
	}
	
	@Override
	public String getRoot() {
		return rootId;
	}
	
	protected Logger getLogger() {
		return logger;
	}

	protected void setLogger(Logger logger) {
		this.logger = logger;
	}

	protected DriveUtils getDriveUtils() {
		return driveUtils;
	}

	protected void setDriveUtils(DriveUtils driveUtils) {
		this.driveUtils = driveUtils;
	}

	protected RequestBuilder getRequestBuilder() {
		return requestBuilder;
	}

	protected void setRequestBuilder(RequestBuilder requestBuilder) {
		this.requestBuilder = requestBuilder;
	}

	protected DriveFileConverter<F> getFileConverter() {
		return fileConverter;
	}

	protected void setFileConverter(DriveFileConverter<F> fileConverter) {
		this.fileConverter = fileConverter;
	}

	protected DriveFileConverter<D> getFolderConverter() {
		return folderConverter;
	}

	protected void setFolderConverter(DriveFileConverter<D> folderConverter) {
		this.folderConverter = folderConverter;
	}

	@Override
	public InputStream getFileContent(String id) throws IOException {
		return new URL(DriveUtils.getClientDownloadUrl(id)).openStream();
	}
	
	@Override
	public void writeFile(String id, String content) {
		writeDriveFile(id, content);
	}
	
	protected void writeDriveFile(String id, String content) {
		String url = DriveUtils.getUploadUrl();
	}
	
	protected List<DriveFile> getDriveFolderContent(String id)
			throws IOException {
		Map<String,List<DriveFile>> map;
		List<DriveFile> files = null;
		String url = driveUtils.generateRequestUrl(DriveMethods.FILE_LIST, id);
		String content;
		try {
			content = RequestBuilder.performGet(url,
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
				files.forEach(file -> {
					if(file.isDirectory()) {
						try {
							file.setChildren(getDriveFolderContent(file.getId()));
						} catch (IOException e) {
							getLogger().error(Util.EXCEPTION_LOG_MESSAGE, e);
						}
					}
				});
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
	
	protected List<F> parseFiles(List<DriveFile> files, boolean recursive
			) throws IOException {
		List<F> fileList = new ArrayList<>();
		for(DriveFile file : files) {
			if(!file.isDirectory()) {
				fileList.add(fileConverter.convert(file));
			} else if(recursive) {
				fileList.addAll(parseFiles(getDriveFolderContent(file.getId()),
						recursive));		
			}
		}
		return fileList;
	}
	
	protected List<D> parseFolders(DriveFile file,
			List<D> dirList) throws IOException {
		if(file == null) {
			throw new IllegalArgumentException();
		}
		if(!file.isDirectory()) {
			return dirList;
		}
		D directory = folderConverter.convert(file);
		dirList.add(directory);
		List<DriveFile> files = getDriveFolderContent(file.getId());
		for(DriveFile cFile : files) {
			if(cFile.isDirectory()) {
				parseFolders(cFile, dirList);		
			} 
		}
		return dirList;
	}	
	
	protected DriveFile getRootFile() throws IOException {
		return getDriveFile(rootId);
	}
	
	protected DriveFile getDriveFile(String id) throws IOException {
		DriveFile file = null;
		String url = driveUtils.generateRequestUrl(DriveMethods.FILE_GET, id);
		String content;
		try {
			content = RequestBuilder
					.performGet(url, "fields", "id,mimeType,title");
			file = new Gson().fromJson(content, DriveFile.class);
			if(file == null) {
				return null;
			}
			if(file.isDirectory()) {
				file.setChildren(getDriveFolderContent(id));
			}
			logger.info("Obtained "+id+" file data by request");
		} catch(RequestException e) {
			getLogger().error(url, e);
			getLogger().error(e.getResponse().getContent());
		} catch(NullPointerException e) {
			getLogger().error(url, e);
		}
		return file;
	}
}
