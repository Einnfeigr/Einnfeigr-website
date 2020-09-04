package main.img;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import main.drive.DriveDao;
import main.drive.DriveFile;
import main.exception.PreviewException;
import main.misc.Util;
import main.misc.filter.ImagePreviewFileFilter;

public class ImagePreviewController {
	
	@Autowired
	static DriveDao driveDao;
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImagePreviewController.class);
	
	public final static int SMALLER_SIDE = 350;
	
	public static void generatePreview(ImageData image) 
			throws IOException,PreviewException {
		//TODO generate google drive previews
		/*
		File output = Util.getFile()
				.replace("/static/img/",
						"static/img/preview/"));
		//No need to create preview if one exists
		if(output.exists()) {
			throw new PreviewException();
		}
		Util.createFile(output);
		ImageIO.write(Util.resizeBySmaller(ImageIO.read(image),  SMALLER_SIDE),
				Util.getExtension(image), output);
		*/
	}
	
	public static void generatePreviews() {
		//TODO generate google drive previews
		/*
		try {
			int count;
			List<ImageData> images = driveDao.getAllFiles();
			for(ImageData image : images) {
				try {
					generatePreview(image);
					count++;
				} catch (IOException | IllegalArgumentException e) {
					logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
				} catch(PreviewException e) {
					continue;
				}
			}
			logger.info("Created "+count+" previews of "
					+images.size()+" files");
		} catch(FileNotFoundException e) {
			logger.error("Error creating previews", e);
		}
		*/
	}

	public static void updatePreviews() {
		//TODO generate google drive previews
		/* try {
			File file = Util.getFile("static/img/preview");
			for(File folder : file.listFiles()) {
				folder.delete();
			}
			generatePreviews();
		} catch(FileNotFoundException e) {
			logger.error("Cannot delete previews", e);
		} */
	}
	
}
