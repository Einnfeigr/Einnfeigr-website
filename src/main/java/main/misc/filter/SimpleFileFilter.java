package main.misc.filter;

import java.io.File;

public class SimpleFileFilter implements FileFilter {

	@Override
	public boolean isValid(File file) {
		return true;
	}

}
