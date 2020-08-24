package main.dashboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import main.misc.Util;

public class ImageStorageService implements StorageService {

	@Override
	public void init() {
		
	}

	@Override
	public void store(MultipartFile file, String path) throws IOException {
		String filename = file.getOriginalFilename().replace("\\", "/");
		if(filename.contains("/")) {
			String[] sp = filename.split("/");
			if(sp.length > 1) {
				filename = sp[sp.length-1];
			}
		}
		File localFile = Util.createFile("static/upload/"+path+filename);
		if(Util.isImage(localFile)) {
			Util.copyImage(Util.getExtension(localFile), file.getInputStream(), localFile);
		} else {
			Util.writeFile(localFile, Util.readFile(file));
		}
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
