package com.sma.db.concrete;

public class Result {

	
	private boolean statusCode;// 0 for success / 1 for error
	private String message;
	private String data;
	
	public Result() {}
	
	public boolean getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(boolean statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}	
}
