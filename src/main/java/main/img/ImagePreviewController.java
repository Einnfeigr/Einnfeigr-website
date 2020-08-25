package main.img;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.misc.Util;
import main.misc.filter.ImagePreviewFileFilter;

public class ImagePreviewController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImagePreviewController.class);
	
	public final static int SMALLER_SIDE = 350;
	
	public static void generatePreviews() {
		File file = Util.getFile("static/img/");
		List<File> images = Util.parseFiles(file, true, new ImagePreviewFileFilter());
		int count = 0;
		for(File image : images) {
			if(image.isDirectory()) {
				logger.warn("previewController tried to parse a directory | "+image);
				continue;
			} 
			if(image.getAbsolutePath().contains("preview")) {
				continue;
			}
			try {
				File output = Util.getFile(image.getAbsolutePath()
						.replace("\\", "/")
						.replace("static/img/", "static/img/preview/"));
				if(output.exists()) {
					continue;
				}
				Util.createFile(output);
				ImageIO.write(Util.resizeBySmaller(ImageIO.read(image), SMALLER_SIDE),
						Util.getExtension(image), output);
				count++;
			} catch (IOException e) {
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
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
