package main.img;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.exception.PreviewException;
import main.misc.Util;
import main.misc.filter.ImagePreviewFileFilter;

public class ImagePreviewController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImagePreviewController.class);
	
	public final static int SMALLER_SIDE = 350;
	
	public static void generatePreview(File image) 
			throws IOException,PreviewException {
		if(image.isDirectory() || !image.exists()) {
			throw new IllegalArgumentException("Invalid file |"+image);
		}
		if(!image.getAbsolutePath().replace("\\", "/")
				.contains("static/img/")) {
			throw new IllegalArgumentException(
					"File must be located at 'static/img' folder | "+image);
		}
		if(image.getAbsolutePath().replace("\\", "/").contains("preview")) {
			throw new PreviewException();
		}
		File output = Util.getFile(image.getAbsolutePath()
				.replace("\\", "/")
				.replace("static/img/", "static/img/preview/"));
		//No need to create preview if one exists
		if(output.exists()) {
			throw new PreviewException();
		}
		Util.createFile(output);
		ImageIO.write(Util.resizeBySmaller(ImageIO.read(image), 
				SMALLER_SIDE),
				Util.getExtension(image), output);
	}
	
	public static void generatePreviews() {
		File file = Util.getFile("static/img/");
		List<File> images = Util.parseFiles(file, true, 
				new ImagePreviewFileFilter());
		int count = 0;
		for(File image : images) {
			try {
				generatePreview(image);
				count++;
			} catch (IOException | IllegalArgumentException e) {
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
			} catch(PreviewException e) {
				continue;
			}
		}
		logger.info("created "+count+" previews of "+images.size()+" files");
	}

	public static void updatePreviews() {
		File file = Util.getFile("static/img/preview/");
		for(File folder : file.listFiles()) {
			folder.delete();
		}
		generatePreviews();
	}
	
}
