package main.img;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import main.drive.dao.PortfolioDriveDao;
import main.drive.dao.PreviewDriveDao;
import main.exception.PreviewException;
import main.misc.Util;

public class ImagePreviewController {
	
	@Autowired
	private PreviewDriveDao previewDao;
	
	@Autowired
	private PortfolioDriveDao portfolioDao;
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImagePreviewController.class);
	
	public final static int SMALLER_SIDE = 350;
	
	public void generatePreview(ImageData image) 
			throws IOException,PreviewException {
		BufferedInputStream bis = new BufferedInputStream(portfolioDao.getFileContent(image.getId()));
		BufferedImage bufferedImage;
		bufferedImage = ImageUtils.resizeBySmaller(ImageIO.read(bis),
				SMALLER_SIDE);
		previewDao.writeFile(image, bufferedImage);
	}
	
	public void generatePreviews() throws IOException {
		try {
			int count = 0;
			List<ImageData> images = portfolioDao.getAllFiles();
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
	}

	public void updatePreviews() {
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
