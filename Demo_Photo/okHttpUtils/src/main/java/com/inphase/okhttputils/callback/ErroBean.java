package com.inphase.okhttputils.callback;

/**
 * @author daimingcheng
 * @version 2016-7-20 下午3:27:34 explain
 */
public class ErroBean {
	// "status" : 400, --状态码，400表示出错
	// "detail":"error info", --错误描述信息
	// "msgflag":"1001" --客户端弹出提示窗口类型
	private int status;
	private String detail;
	private int msgflag;

	public ErroBean(int status, String detail, int msgflag) {
		super();
		this.status = status;
		this.detail = detail;
		this.msgflag = msgflag;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getMsgflag() {
		return msgflag;
	}

	public void setMsgflag(int msgflag) {
		this.msgflag = msgflag;
	}

}
