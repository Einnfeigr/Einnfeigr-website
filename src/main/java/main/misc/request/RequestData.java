package main.misc.request;

public class RequestData {

	private String address;
	private String method;
	private int calls;
	private ResponseData response;
	
	public RequestData(String address, String method) {
		this.address = address;
		this.method = method;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public ResponseData getResponse() {
		calls += 1;
		return response;
	}

	public void setResponse(ResponseData response) {
		this.response = response;
	}

	public int getCalls() {
		return calls;
	}

	public void reset() {
		calls = 0;
		response = null;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof RequestData)) {
			return false;
		}
		RequestData data = (RequestData)o;
		return data.getAddress().equals(this.address)
				&& data.getMethod().equals(this.method); 
	}
	
}
