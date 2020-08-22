package main.dashboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipController {

	public static void packZip(File zipFile, List<ZipFileEntry> files) throws IOException {
		if(!isZip(zipFile)) {
			throw new IOException("Given file is not zip | "+zipFile.getName()); 
		}
        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
        	ZipEntry entry;
        	StringBuilder content = new StringBuilder("");
        	for(ZipFileEntry file : files) {
        		content.delete(0, content.length());
    			entry = new ZipEntry(file.getRelativePath());
        		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getFile())))) {
        			zos.putNextEntry(entry);
        			while(br.ready()) {
        				content.append(br.readLine());
        			}
        			for(int b : content.toString().toCharArray()) {
        				zos.write(b);
        			}
        			zos.closeEntry();
        		}
        	}
        }
	}
	
	public static List<File> unpackZip(File zipFile, String path) throws IOException {
		if(!isZip(zipFile)) {
			throw new IOException("Given file is not zip | "+zipFile.getName());
		}
		List<File> files = new ArrayList<>();
		try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry entry;
			while((entry = zis.getNextEntry()) != null) {
				File entryFile = new File(path+entry.getName());
				files.add(entryFile);
				if(!entryFile.exists()) {
					entryFile.createNewFile();
				}
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(entryFile), "UTF-8"));
					for(int c = zis.read(); c != -1; c = zis.read()) {
						bw.write(c);
					}
				bw.flush();
				zis.closeEntry();
				bw.close();
			}
		} catch(EOFException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	private static boolean isZip(File file) {
		String[] sp = file.getName().split("\\.");
		if(sp.length < 2) {
			return false;
		} 
		return sp[sp.length-1].equals("zip");
	}
	
}
