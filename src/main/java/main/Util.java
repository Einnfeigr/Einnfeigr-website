package main;

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

import org.springframework.util.ResourceUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import main.exception.TemplateException;
import main.pojo.TemplateData;

public class Util {
    
    public static String compileTemplate(String path, TemplateData data) throws IOException {
    	File template = new File(path);
    	if(!template.exists()) {
    		throw new FileNotFoundException("File '"+path+"' cannot be found");
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
    
    public static String toAbsoluteUrl(String url) throws FileNotFoundException {
    	return ResourceUtils.getURL("classpath:"+url).getPath();
    }
    
    public static String toRelativeUrl(String url) throws FileNotFoundException {
    	return url.replace(ResourceUtils.getURL("classpath:").getFile().substring(1).replace("/", "\\"), "");
    }
    
    public static void createFile(String relativeUrl) throws IOException {
    	new File(ResourceUtils.getURL("classpath:").getPath()+relativeUrl).createNewFile();
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
    
    public static String readFile(File file) throws IOException {
    	StringBuilder content = new StringBuilder("");
    	if(!file.exists()) {
    		throw new FileNotFoundException();
    	}
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
}
