package main.img;

import java.io.File;
import java.io.FileNotFoundException;
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
		List<ImageData> indexedImages;
		File file = new File(Util.toAbsoluteUrl("static/img/portfolio/sections"));
		indexedImages = appendImages(file);
		merge(images, indexedImages);
		Comparator<ImageData> comparator = (d1, d2) -> d1.compareTo(d2);
		images.sort(comparator);
		dao.save(images);
	}

	private List<ImageData> appendImages(File file) {
		if(!file.isDirectory()) {
			return null;
		} 
		List<ImageData> dataList = new ArrayList<>();
		for(File cFile : file.listFiles()) {
			try {
				if(Util.isImage(cFile)) {				
					ImageData data = new ImageData();
					data.setFile(cFile);
					data.setIndexingTime(System.currentTimeMillis());
					dataList.add(data);
				} else if(cFile.isDirectory()) {
					dataList.addAll(appendImages(cFile));
				}
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		} 
		return dataList;
	}
	
	private void merge(List<ImageData> images, List<ImageData> indexedImages) {
		//clean of unindexed images
		for(ImageData data : images) {
			if(!indexedImages.contains(data)) {
				images.remove(data);
			}
		}
		//append new indexed images
		for(ImageData data : indexedImages) {
			if(!images.contains(data)) {
				images.add(data);
			}
		}
	}
}
