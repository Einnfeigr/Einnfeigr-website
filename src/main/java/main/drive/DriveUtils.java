package main.drive;

public class DriveUtils {
	
	private static String key;
	public static final String rootId = "1zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa";
	
	public DriveUtils(String key) {
		if(key.toLowerCase().equals("google drive api key")) {
			key = System.getenv("GoogleDriveApiKey");
		}
		DriveUtils.key = key;
	}
	
	public static String generateRequestUrl(DriveMethods method, String id, 
			String params) {
		switch(method.ordinal()) {
		case(0):
			return "https://www.googleapis.com/drive/v2/files?q=%27"+id
					+"%27+in+parents&key="+key+params;
		case(1):
			return "https://www.googleapis.com/drive/v2/files/"+id
					+"?key="+key+params;
		case(2):
			return "https://www.googleapis.com/upload/drive/v2/files";
		default:
			return null;	
		}
		
	}   
	
	public static String getDownloadUrl(String id) {
		return "https://drive.google.com/uc?id="+id+"&export=download";
	}
}
