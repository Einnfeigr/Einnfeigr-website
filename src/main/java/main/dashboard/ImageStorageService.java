package main.dashboard;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

//Iceboxed
public class ImageStorageService implements StorageService {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImageStorageService.class);
	
	@Override
	public void init() {
		
	}

	@Override
	public void store(MultipartFile file, String path) throws IOException {
		/*
		String filename = file.getOriginalFilename().replace("\\", "/");
		if(filename.contains("/")) {
			String[] sp = filename.split("/");
			if(sp.length > 1) {
				filename = sp[sp.length-1];
			}
		}
		if(!path.endsWith("/")) {
			path += "/";
		}
		File localFile = Util.getFile("static/upload/"+path+filename);
		if(Util.isImage(localFile)) {
			if(path.contains("img/portfolio/albums/")) {
				localFile = Util.getFile("static/"+path+filename);
			}
			Util.createFile(localFile);
			Util.copyImage(Util.getExtension(localFile), file.getInputStream(),
					localFile);
		} else {
			Util.createFile(localFile);
			Util.writeFile(localFile, Util.readFile(file));
		}
		try {
			ImagePreviewController.generatePreview(localFile);
		} catch (PreviewException e) {
			logger.warn("Uploaded image preview exists", e);
		}
		*/
	}

	@Override
	public Stream<Path> loadAll() {
		return null;
	}

	@Override
	public Path load(String filename) {
		return null;
	}

	@Override
	public Resource loadAsResource(String filename) {
		return null;
	}

	@Override
	public void deleteAll() {
		
	}

}
