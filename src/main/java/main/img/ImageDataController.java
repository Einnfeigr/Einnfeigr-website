package main.img;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import main.misc.Util;
import main.misc.filter.ImagePreviewFileFilter;

public class ImageDataController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataController.class);
	
	List<ImageData> images;

	@Autowired
	private ImageDataDao dao;
	
	public ImageDataController() {
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
		List<File> files = null;
		/*try {
			files = Util.parseFiles(Util.getFile(COPY_PATH), true,
					new ImagePreviewFileFilter());
			if(files == null || files.size() == 0) {
				logger.warn("Found 0 files");
			} else {
				while(files.size() > 10) {
					files.remove(files.size()-1);
				}
			}
		} catch(FileNotFoundException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		}*/
		return files;
	}
	
	public void loadImages() throws IOException { 
		List<ImageData> indexedImages;
		File file = new File(
				Util.toAbsoluteUrl("static/img/portfolio/sections"));
		indexedImages = parseImagesData(file);
		if(indexedImages != null) {
			logger.info("indexed "+indexedImages.size()+" images");
			merge(images, indexedImages);
			saveImages(images);
		} else {
			logger.warn("indexed 0 images");
		}
	}
	
	private void saveImages(List<ImageData> images) {
		/* images.forEach(i -> {
			File previewFile = new File(Util.toAbsoluteUrl(
					PREVIEW_PATH+i.getName()));
			File file = new File(Util.toAbsoluteUrl(
					COPY_PATH+i.getName()));
			Util.createFile(previewFile);	
			Util.createFile(file);
			try {
				Util.copyImage(i.getFile(), file);
				ImageIO.write(Util.resizeBySmaller(ImageIO.read(i.getFile()),
						ImagePreviewController.SMALLER_SIDE),
						Util.getExtension(i.getFile()), previewFile);
			} catch (IOException e) {
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			} 
		}); */
	}
	
	private List<ImageData> parseImagesData(File file) {
		if(!file.isDirectory()) {
			return null;
		}
		List<ImageData> dataList = new ArrayList<>();
		/*
		for(File cFile : file.listFiles()) {
			if(Util.isImage(cFile)) {				
				ImageData data = new ImageData();
				data.setFile(cFile);
				data.setIndexingTime(System.currentTimeMillis());
				dataList.add(data);
			} else if(cFile.isDirectory()) {
				dataList.addAll(parseImagesData(cFile));
			}
		} */
		return dataList; 
	}
	
	private void merge(List<ImageData> images, List<ImageData> indexedImages) {
		/*
		List<Integer> toRemove = new ArrayList<>();
		//clean of unindexed images
		for(ImageData data : images) {
			if(!indexedImages.contains(data) || !data.getFile().exists()) {
				toRemove.add(images.indexOf(data));
			}
		}
		for(int i : toRemove) {
			if(i < images.size()) {
				images.remove(i);
			}
		}
		//append new indexed images
		for(ImageData data : indexedImages) {
			if(!images.contains(data)) {
				images.add(data);
			}
		}
		*/
	}
}
