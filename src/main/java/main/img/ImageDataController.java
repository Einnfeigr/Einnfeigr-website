package main.img;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import main.misc.Util;

public class ImageDataController {

	List<ImageData> images;

	private ImageDataDao dao;
	
	public ImageDataController(ImageDataDao dao) {
		this.dao = dao;
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
	
	public List<ImageData> getImages() {
		return images;
	}
	
	public List<String> getImagePaths(String path) throws FileNotFoundException {
		List<String> paths = new ArrayList<>();
		for(ImageData data : images) {
			paths.add(path+data.getPath());
		}
		return paths;
	}
	
	public void loadImages() throws IOException { 
		//TODO refactor
		if(images == null) {
			images = new ArrayList<>();
		}
		List<ImageData> indexedImages;
		File file = new File(Util.toAbsoluteUrl("static/img/portfolio/sections"));
		indexedImages = appendImages(file);
		if(indexedImages == null) {
			//TODO refactor
			return;
		}
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
