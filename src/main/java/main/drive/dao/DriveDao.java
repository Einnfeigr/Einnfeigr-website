package main.drive.dao;

import java.io.IOException;
import java.util.List;

public interface DriveDao<F, D> {

	List<F> getAllFiles() throws IOException;
	
	List<D> getAllFolders() throws IOException;
	
	String getRoot() throws IOException;
	
	F getFile(String id) throws IOException;
	
	List<F> getFolderContent(String id) throws IOException;
	
}
