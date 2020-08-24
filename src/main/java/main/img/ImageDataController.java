package main.img;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.misc.Util;
import main.misc.filter.ImagePreviewFileFilter;

public class ImageDataController {
	
	private static Logger logger = LoggerFactory.getLogger(ImageController.class);
	
	List<ImageData> images;

	private ImageDataDao dao;

	private static final String PREVIEW_PATH = "static/img/preview/latest/";
	
	public ImageDataController(ImageDataDao dao) {
		this.dao = dao;
		try {
			images = dao.getAll();
		} catch(Exception e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		} finally {
			if(images == null) {
				images = new ArrayList<>();
			}
		}
	}

	public static List<File> getLatestImages() {
		return Util.parseFiles(Util.getFile(PREVIEW_PATH), true, new ImagePreviewFileFilter());
	}
	
	public void loadImages() throws IOException { 
		List<ImageData> indexedImages;
		File file = new File(Util.toAbsoluteUrl("static/img/portfolio/sections"));
		indexedImages = parseImagesData(file);
		if(indexedImages != null) {
			logger.info("indexed "+indexedImages.size()+" images");
			merge(images, indexedImages);
			Comparator<ImageData> comparator = (d1, d2) -> d1.compareTo(d2);
			images.sort(comparator);
			dao.save(images);
			saveImages(images);
		} else {
			logger.warn("indexed 0 images");
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
		List<Integer> toRemove = new ArrayList<>();
		//clean of unindexed images
		for(ImageData data : images) {
			if(!indexedImages.contains(data)) {
				toRemove.add(images.indexOf(data));
			}
		}
		for(int i : toRemove) {
			images.remove(i);
		}
		//append new indexed images
		for(ImageData data : indexedImages) {
			if(!images.contains(data)) {
				images.add(data);
			}
		}
	}
}
