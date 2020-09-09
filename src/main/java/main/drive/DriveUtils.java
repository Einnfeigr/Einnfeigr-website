package main.drive;

public class DriveUtils {
	
	private static String authToken;
	private static String key;
	public static final String rootId = "1zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa";
	
	public DriveUtils(String key) {
		if(key.toLowerCase().equals("google drive api key")) {
			key = System.getenv("GoogleDriveApiKey");
		}
		DriveUtils.key = key;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
	public String generateRequestUrl(DriveMethods method, String id) {
		switch(method) {
		case FILE_LIST:
			return "https://www.googleapis.com/drive/v2/files?q=%27"+id
					+"%27+in+parents&key="+key;
		case FILE_GET:
			return "https://www.googleapis.com/drive/v2/files/"+id
					+"?key="+key;
		case FILE_INSERT:
			return "https://www.googleapis.com/upload/drive/v2/files";
		default:
			return null;	
		}
		
	}   
	
	public static String getDownloadUrl(String id) {
		return "https://drive.google.com/uc?id="+id+"&export=download";
	}
}
