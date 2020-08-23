package main.dashboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import main.misc.Util;

public class ZipImageStorageService implements StorageService {

	@Override
	public void init() {
		
	}

	@Override
	public void store(MultipartFile file, String path) throws IOException {
		String filename = file.getOriginalFilename().replace("\\", "/");
		String[] sp;
		if(filename.contains("/")) {
			sp = filename.split("/");
			if(sp.length > 1) {
				filename = sp[sp.length-1];
			}
		}
		File zipFile = new File(Util.toAbsoluteUrl("static/temp/"+filename));
		if(zipFile.exists()) {
			zipFile.delete();
		}
		if(!zipFile.exists()) {
			if(!zipFile.getParentFile().exists()) {
				zipFile.getParentFile().mkdirs();
			}
			zipFile.createNewFile();
		}
		Util.writeFile(zipFile, Util.readFile(file));
		List<File> files = ZipController.unpackZip(zipFile, path);
		for(File cFile : files) {
			System.out.println(cFile.getAbsolutePath());
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
