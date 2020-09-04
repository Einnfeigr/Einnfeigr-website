package main.img;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {
	
	public static BufferedImage resizeByHeight(BufferedImage image, int height)
			throws IOException {
		return Thumbnails.of(image)
				.height(height)
				.keepAspectRatio(true)
				.useOriginalFormat()
				.asBufferedImage();
	}
	
	public static BufferedImage resizeByWidth(BufferedImage image, int width)
			throws IOException {
		return Thumbnails.of(image)
				.width(width)
				.keepAspectRatio(true)
				.useOriginalFormat()
				.asBufferedImage();
	}

    public static BufferedImage resizeByLarger(BufferedImage image, 
    		int larger) throws IOException {
    	int height = image.getHeight();
    	int width = image.getWidth();
    	if(height < width) {
        	return Thumbnails.of(image)
        			.width(larger)
        			.keepAspectRatio(true)
        			.useOriginalFormat()
        			.asBufferedImage();
    	} else if(height > width) {
        	return Thumbnails.of(image)
        			.height(larger)
        			.keepAspectRatio(true)
        			.useOriginalFormat()
        			.asBufferedImage();
    	} else {
        	return Thumbnails.of(image)
        			.size(larger, larger)
        			.keepAspectRatio(true)
        			.useOriginalFormat()
        			.asBufferedImage();
    	}
    }
    
    public static BufferedImage resizeBySmaller(BufferedImage image, 
    		int smaller) throws IOException {
    	int height = image.getHeight();
    	int width = image.getWidth();
    	if(height > width) {
        	return Thumbnails.of(image)
        			.width(smaller)
        			.keepAspectRatio(true)
        			.useOriginalFormat()
        			.asBufferedImage();
    	} else if(height < width) {
        	return Thumbnails.of(image)
        			.height(smaller)
        			.keepAspectRatio(true)
        			.useOriginalFormat()
        			.asBufferedImage();
    	} else {
        	return Thumbnails.of(image)
        			.size(smaller, smaller)
        			.keepAspectRatio(true)
        			.useOriginalFormat()
        			.asBufferedImage();
    	}
    }
    
	public static BufferedImage resize(BufferedImage image, int width,
			int height) throws IOException {
		return Thumbnails.of(image)
				.size(width, height)
				.keepAspectRatio(true)
				.useOriginalFormat()
				.asBufferedImage();
	}   

}
