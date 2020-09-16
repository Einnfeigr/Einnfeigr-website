package main.drive.dao;

import java.io.IOException;

public interface WritableDriveDao<F, T> {

	void writeFile(F file, T content) throws IOException;
	
}
