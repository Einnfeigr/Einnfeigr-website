package main.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import main.misc.Util;
import main.misc.filter.ImagePreviewFileFilter;

public class ImagePreviewController {

	public static void generatePreviews() {
		File file = Util.getFile("static/img/");
		List<File> images = Util.parseFiles(file, true, new ImagePreviewFileFilter());
		for(File image : images) {
			if(image.isDirectory()) {
				continue;
			}
			try {
				File output = Util.createFile(image.getAbsolutePath()
						.replace("\\", "/")
						.replace("static/img/", "static/img/preview/"));
				if(output.exists()) {
					continue;
				}
				BufferedImage bufferedImage = ImageIO.read(image);
				double ratio = (double)bufferedImage.getHeight()/
						(double)bufferedImage.getWidth();
				ImageIO.write(Util.resize(bufferedImage, 300, (int)(300*ratio)),
						Util.getExtension(image), output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void resetPreviews() {
		File file = Util.getFile("static/img/preview/");
		for(File folder : file.listFiles()) {
			folder.delete();
		}
	}
	
}
