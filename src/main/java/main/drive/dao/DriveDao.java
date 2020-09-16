package main.drive.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DriveDao<F, D> {

	String getRoot();	
	F getFile(String id) throws IOException;	
	List<F> getAllFiles() throws IOException;
	List<D> getAllFolders() throws IOException;
	List<F> getFolderContent(String id) throws IOException;
	InputStream getFileContent(String id) throws IOException;
	
}
