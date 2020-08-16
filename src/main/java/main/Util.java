package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    	if(template.isFile()) {
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
}
