package main.drive.dao;

import java.io.IOException;

import main.drive.DriveFile;

public interface DriveFileConverter<T> {

	T convert(DriveFile file) throws IOException;
	
}
