package com.inphase.okhttputils.model;

public class Result {
	// {
	// "status" : 200,
	// "detail":"购买成功。"
	// }
	// "status":400, --状态码，400表示出错
	// "status":600, --状态码，400表示出错
	// "detail":"error info",--错误描述信息
	private String status;
	private String detail;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
}
