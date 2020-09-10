package main.drive;

public class DriveUtils {
	
	private TokenBunch tokenBunch;
	private static String key;
	public static final String rootId = "1zTDcH9LuuZ0KUI2c3K6f-r5pAUt_mrpa";
	
	public DriveUtils() {
		tokenBunch = new TokenBunch();
		DriveUtils.key = System.getenv("drive.apiKey");
	}
	
	public String getUserCode() {
		return tokenBunch.getUserCode();
	}

	public void setUserCode(String userCode) {
		tokenBunch.setUserCode(userCode);
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
	
	public String getAccessToken() {
		return tokenBunch.getAccessToken();
	}
	
	public static String getDownloadUrl(String id) {
		return "https://drive.google.com/uc?id="+id+"&export=download";
	}
}
