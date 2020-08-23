package main.misc.filter;

import java.io.File;

public class NotADirectoryFileFilter implements FileFilter {

	@Override
	public boolean isValid(File file) {
		return !file.isDirectory();
	}

}
