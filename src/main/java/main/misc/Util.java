package main.misc;

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
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImagePreviewController.class);
	public final static String EXCEPTION_LOG_MESSAGE = 
			"Exception has been caught";
	
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
    		copyImage(getExtension(src.getName()), is, dest);
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
    
    public static String getExtension(String name) {
    	String[] spl = name.split("\\.");
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

}
