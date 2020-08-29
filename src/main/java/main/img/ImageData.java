package main.img;

public class ImageData implements Comparable<ImageData> {

	private String id;
	private String createdDate;



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


	@Override
	public int compareTo(ImageData o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
