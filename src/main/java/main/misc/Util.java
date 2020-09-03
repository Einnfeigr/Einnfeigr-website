package main.misc;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import main.img.ImagePreviewController;
import main.misc.filter.FileFilter;
import main.misc.filter.SimpleFileFilter;

public class Util {
	
	@Autowired
	ServletContext servletContext;
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImagePreviewController.class);
	public final static String EXCEPTION_LOG_MESSAGE = 
			"Exception has been caught";
	
	private final static ClassLoader classLoader = Util.class.getClassLoader();
	
    public static boolean isAbsolute(File file) throws FileNotFoundException {
    	return isAbsolute(file.getAbsolutePath());
    }
    
    public static boolean isAbsolute(String url) throws FileNotFoundException {
		return url.replace("\\", "/").contains(
				ResourceUtils.getURL("classpath:").getFile()
				.substring(1)
				.replace("\\", "/"));
    }
    
    public static String toAbsoluteUrl(String url) {
    	try {
        	if(isAbsolute(url)) {
        		return url;
        	}
    		return ResourceUtils.getURL("classpath:").getPath()+url;
    	} catch(IOException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public static String toRelativeUrl(String url) {
    	try {
    		if(!isAbsolute(url)) {
    			return url;
    		}
    		return url.replace("\\", "/").replace(
    				ResourceUtils.getURL("classpath:").getFile()
    				.substring(1)
    				.replace("\\", "/"), "/");
    	} catch(IOException e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    public static String toRelativeUrl(File file) {
    	return toRelativeUrl(file.getAbsolutePath().replace("\\", "/"));
    }
    
    public static List<File> parseFiles(File file) {
    	return parseFiles(file, true, new SimpleFileFilter());
    }
    
    public static List<File> parseFiles(File file, 
    		boolean parseSubdirectories) {
    	return parseFiles(file, parseSubdirectories, new SimpleFileFilter());
    }
    
    public static List<File> parseFiles(File file, boolean parseSubdirectories,
    		FileFilter filter) {
    	if(!file.isDirectory() || !file.exists()) {
    		return null;
    	}
    	List<File> files = new ArrayList<>();
    	if(filter.isValid(file)) {
    		files.add(file);
    	}
    	for(File cFile : file.listFiles()) {
    		if(cFile.isDirectory()) {
    			if(!parseSubdirectories) {
    				continue;
    			}
    			List<File> cFiles = parseFiles(cFile, parseSubdirectories,
    					filter);
    			if(cFiles != null) {
    				files.addAll(cFiles);
    			}
    		} else if(filter.isValid(cFile)) {
    			files.add(cFile);	
    		}
    	}
    	return files;
    }
    
    public static File getFile(String path) throws FileNotFoundException {
    	return new File(servletContext.getRealPath("")+path);
    }
    
    public static File createFile(File file) {
    	try {
    		if(file.exists()) {
    			file.delete();
    		}
	    	if(file.isDirectory()) {
	    		file.mkdirs();
	    	} else {
	    		file.getParentFile().mkdirs();
	    		file.createNewFile();
	    	}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	return file;
    }
    
    public static File createFile(String relativeUrl) {
    	return createFile(new File(toAbsoluteUrl(relativeUrl)));
    }
    
    public static boolean isImage(File file) {
    	String[] sp = file.getName().split("\\.");
    	if(sp.length < 2) {
    		return false;
    	}
    	switch(sp[sp.length-1]) {
    		case("jpg"):
    		case("jpeg"):
    		case("png"):
    		case("gif"):
    		case("webp"):
    			return true;
    		default:
    			return false;
    	}
    }
    
    public static String toUpperCase(String text) {
    	String separator;
    	StringBuilder sb;
    	if(text.contains("/")) {
    		separator = "/";
    	} else if(text.contains("\\")) {
    		separator = "\\";
    	} else if(text.contains(".")) {
    		separator = ".";
    	} else {
    		 return Character.toUpperCase(text.charAt(0))+text.substring(1);
    	}
    	sb = new StringBuilder("");
    	for(String string : text.split(separator)) {
    		if(string.length() < 1) {
    			continue;
    		}
    		string = Character.toUpperCase(string.charAt(0))
    				+string.substring(1);
    		sb.append(string+separator);
    	}
    	return sb.toString();
    }
    
    public static void copyFile(File src, File dest) throws IOException {
    	if(Util.isImage(src) && Util.isImage(dest)) {
    		copyImage(src, dest);
    		return;
    	}
		Files.copy(src.toPath(), dest.toPath(), 
				StandardCopyOption.REPLACE_EXISTING, 
				StandardCopyOption.COPY_ATTRIBUTES);
    }
    
    public static void copyImage(File src, File dest) 
    		throws IllegalArgumentException {
    	if(!Util.isImage(src) || !Util.isImage(dest)) {
    		throw new IllegalArgumentException(
    				"One or both passed files are not images");
    	}
    	if(!src.exists()) {
    		throw new NullPointerException(
    				"Can't find file '"+src.getAbsolutePath()+"'");
    	}
    	try(InputStream is = new FileInputStream(src)) {
    		copyImage(getExtension(src), is, dest);
    	} catch(Exception e) {
    		logger.error(EXCEPTION_LOG_MESSAGE, e);
    		throw new IllegalArgumentException(e);
		}
    }
    
    public static void copyImage(String extension, InputStream stream,
    		File dest) {
    	BufferedImage image = null;
    	try {
	        image = ImageIO.read(stream);
	    	ImageIO.write(image, extension, dest);
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public static String getExtension(File file) {
    	String[] spl = file.getName().split("\\.");
    	if(spl.length < 2) {
    		return null;
    	}
    	return spl[spl.length-1];
    }
    
    public static String readFile(MultipartFile file) throws IOException {
    	StringBuilder content = new StringBuilder("");
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(
    			file.getInputStream()))) {
    		while(br.ready()) {
    			content.append(br.readLine());
    		}
    	}
    	return content.toString();
    }
    
    public static String readFile(File file) throws IOException {
    	if(!file.exists()) {
    		throw new FileNotFoundException();
    	}
    	StringBuilder content = new StringBuilder("");
    	for(String string : Files.readAllLines(file.toPath())) {
    		content.append(string+"\n");
    	}
    	return content.toString();
    }
    
    public static void writeFile(File file, String content) throws IOException {
    	if(!file.exists()) {
    		throw new FileNotFoundException();
    	}
    	if(content == null) {
    		throw new NullPointerException();
    	}
    	try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
    			new FileOutputStream(file)))) {
    		bw.write(content);
    	}
    }
    
    public static List<String> parseNames(File file, File baseFile) {
    	if(!file.exists() || !file.isDirectory()) {
    		return null;
    	}
    	List<String> names = new ArrayList<>();
    	for(File cFile : file.listFiles()) {
    		if(cFile.isDirectory()) {
    			names.addAll(parseNames(cFile, baseFile));
    		}
    		names.add(file.getAbsolutePath().replace(baseFile.getAbsolutePath(),
    				""));
    	}
    	return names;
    }
    
    //TODO refactor
    public static BufferedImage resizeByLarger(BufferedImage image, 
    		int larger) {
    	double ratio;
    	int height = image.getHeight();
    	int width = image.getWidth();
    	if(height < width) {
    		ratio = (double) height / (double) width;
    		width = larger;
    		height = (int) (width*ratio);
    	} else if(height > width){
    		ratio = (double) width / (double) height;
    		height = larger;
    		width = (int) (height*ratio);
    	} else {
    		width = larger;
    		height = larger;
    	}
    	return resize(image, width, height);
    }
    
    public static BufferedImage resizeBySmaller(BufferedImage image, 
    		int smaller) {
    	double ratio;
    	int height = image.getHeight();
    	int width = image.getWidth();
    	if(height > width) {
    		ratio = (double) height / (double) width;
    		width = smaller;
    		height = (int) (width*ratio);
    	} else if(height < width) {
    		ratio = (double) width / (double) height;
    		height = smaller;
    		width = (int) (height*ratio);
    	} else {
    		width = smaller;
    		height = smaller;
    	}
    	return resize(image, width, height);
    }
    
	public static BufferedImage resize(BufferedImage image, int width,
			int height) { 
	    int w = image.getWidth(), h = image.getHeight();
	    int type = image.getType() == 0? 
	    		BufferedImage.TYPE_INT_ARGB : image.getType();
	    BufferedImage resizedImage = new BufferedImage(width, height, type);
	    Graphics2D g = resizedImage.createGraphics();
	    g.setComposite(AlphaComposite.Src);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING,
	    		RenderingHints.VALUE_RENDER_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    		RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	    g.scale((double)width/w,(double)height/h);
	    g.drawRenderedImage(image, null);
	    g.dispose();
	    return resizedImage; 
	}   

}
