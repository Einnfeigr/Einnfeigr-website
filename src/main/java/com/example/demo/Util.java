package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.springframework.util.ResourceUtils;

import com.example.demo.pojo.TemplateData;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class Util {
    
    public static String compileTemplate(String path, TemplateData data) throws IOException {
    	if(!new File(path).exists()) {
    		throw new FileNotFoundException("File '"+path+"' cannot be found");
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
    	return url.replace(ResourceUtils.getURL("classpath:").getFile(), "");
    }
}
