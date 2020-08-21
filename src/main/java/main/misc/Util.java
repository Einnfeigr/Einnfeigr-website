package main.misc;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import main.exception.TemplateException;
import main.pojo.TemplateData;

public class Util {
    
    public static String compileTemplate(String path, TemplateData data) throws IOException {
    	path += ".mustache";
    	File template = new File(path);
    	if(!template.exists()) {
    		template = new File(Util.toAbsoluteUrl(path));
    		if(!template.exists()) {
    			throw new FileNotFoundException("File '"+path+"' cannot be found");
    		}
    	}
    	if(!template.isFile()) {
    		throw new TemplateException("Template '"+path+"' must be a file");
    	}
    	MustacheFactory factory = new DefaultMustacheFactory();
    	Mustache mustache = factory.compile(path);
    	StringWriter writer = new StringWriter();
    	mustache.execute(writer, data).flush();
    	return writer.toString();
    }
    
    public static String toAbsoluteUrl(String url) {
    	try {
    		return ResourceUtils.getURL("classpath:").getPath()+url;
    	} catch(IOException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public static String toRelativeUrl(String url) {
    	try {
    		return url.replace(ResourceUtils.getURL("classpath:").getFile()
    				.substring(1).replace("/", "\\"), "");
    	} catch(IOException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public static List<File> parseFiles(File file, boolean parseSubdirectories) {
    	if(!file.isDirectory() || !file.exists()) {
    		return null;
    	}
    	List<File> files = new ArrayList<>();
    	files.add(file);
    	for(File cFile : file.listFiles()) {
    		if(cFile.isDirectory()) {
    			if(!parseSubdirectories) {
    				continue;
    			}
    			List<File> cFiles = parseFiles(cFile, parseSubdirectories);
    			if(cFiles != null) {
    				files.addAll(cFiles);
    			}
    		} else {
    			files.add(cFile);
    		}
    	}
    	return files;
    }
    
    public static String toRelativeUrl(File file) {
    	return toRelativeUrl(file.getAbsolutePath());
    }
    
    public static File getFile(String path) {
    	return new File(toAbsoluteUrl(path));
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
    
    public static String UrlToUpperCase(String url) {
    	String separator;
    	StringBuilder sb;
    	if(url.contains("/")) {
    		separator = "/";
    	} else if(url.contains("\\")) {
    		separator = "\\";
    	} else {
    		 return Character.toUpperCase(url.charAt(0))+url.substring(1);
    	}
    	sb = new StringBuilder("");
    	for(String string : url.split(separator)) {
    		string = Character.toUpperCase(string.charAt(0))+string.substring(1);
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
				StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    }
    
    public static void copyImage(File src, File dest) {
    	if(!Util.isImage(src) || !Util.isImage(dest)) {
    		throw new IllegalArgumentException("One or both passed files are not images");
    	}
    	BufferedImage image = null;
    	try {
	        image = ImageIO.read(src);
	    	ImageIO.write(image, getExtension(src), dest);
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
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(
    			new FileInputStream(file)))) {
    		while(br.ready()) {
    			content.append(br.readLine());
    		}
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
    		names.add(file.getAbsolutePath().replace(baseFile.getAbsolutePath(), ""));
    	}
    	return names;
    }
}
