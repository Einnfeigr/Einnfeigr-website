package main.drive;

import java.io.IOException;

public interface DriveFileConverter<T> {

	T convert(DriveFile file) throws IOException;
	
}
