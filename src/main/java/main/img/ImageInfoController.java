package main.img;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.Util;

@Component
public class ImageInfoController {

	List<ImageData> images;

	@Autowired
	ImageInfoDao dao;
	
	public ImageInfoController(ImageInfoDao dao) {
		try {
			images = dao.getAll();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(images == null) {
				images = new ArrayList<>();
			}
		}
	}
	
	public List<ImageData> getImagesByDate() {
		return images;
	}
	
	public void loadImages() throws IOException {
		File file = new File(Util.toAbsoluteUrl("static/img/portfolio/sections"));
		appendImages(file);
		Comparator<ImageData> comparator = (d1, d2) -> d1.compareTo(d2);
		images.sort(comparator);
		dao.save(images);
	}

	private void appendImages(File file) {
		if(!file.isDirectory()) {
			return;
		} 
		for(File cFile : file.listFiles()) {
			if(Util.isImage(cFile) && !isIndexed(file)) {				
				ImageData data = new ImageData();
				data.setFile(cFile);
				data.setIndexingTime(System.currentTimeMillis());
				images.add(data);
			} else if(cFile.isDirectory()) {
				appendImages(cFile);
			}
		}
	}
	
	private boolean isIndexed(File file) {
		for(ImageData data : images) {
			if(data.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
				return true;
			}
		}
		return false;
	}
}
