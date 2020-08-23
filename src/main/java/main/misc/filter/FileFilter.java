package main.misc.filter;

import java.io.File;

public interface FileFilter {
	
	boolean isValid(File file);
}
