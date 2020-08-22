package main.img;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import main.misc.Util;

public class ImageDataController {

	List<ImageData> images;

	private ImageDataDao dao;

	private static final String PREVIEW_PATH = "static/img/preview/latest/";
	
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

	public static List<File> getLatestImages() {
		return Util.parseFiles(Util.getFile(PREVIEW_PATH), true);
	}
	
	public void loadImages() throws IOException { 
		List<ImageData> indexedImages;
		File file = new File(Util.toAbsoluteUrl("static/img/portfolio/sections"));
		indexedImages = parseImagesData(file);
		if(indexedImages != null) {
			merge(images, indexedImages);
			Comparator<ImageData> comparator = (d1, d2) -> d1.compareTo(d2);
			images.sort(comparator);
			dao.save(images);
			saveImages(images);
		}
	}
	
	private void saveImages(List<ImageData> images) {
		images.forEach(i -> {
			File file = new File(Util.toAbsoluteUrl(PREVIEW_PATH+i.getName()));
			Util.createFile(file);
			Util.copyImage(i.getFile(), file);
		});
	}
	
	private List<ImageData> parseImagesData(File file) {
		if(!file.isDirectory()) {
			return null;
		} 
		List<ImageData> dataList = new ArrayList<>();
		for(File cFile : file.listFiles()) {
			if(Util.isImage(cFile)) {				
				ImageData data = new ImageData();
				data.setFile(cFile);
				data.setIndexingTime(System.currentTimeMillis());
				dataList.add(data);
			} else if(cFile.isDirectory()) {
				dataList.addAll(parseImagesData(cFile));
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
