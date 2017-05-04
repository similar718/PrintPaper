package com.inphase.okhttputils.model;

import com.inphase.okhttputils.callback.ErroBean;

public class CustomException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5069961623271201579L;
	private int msgflag = 0;
	private int status = 0;
	private String errMessage;

	public CustomException(String errMsg) {
		super(errMsg);
	}

	public CustomException(int errCode, String errMsg, int msgflag) {
		this(errMsg);
		this.status = errCode;
		this.errMessage = errMsg;
		this.msgflag = msgflag;
	}

	public CustomException(ErroBean errMsg) {
		this(errMsg.getDetail());
		this.status = errMsg.getStatus();
		this.errMessage = errMsg.getDetail();
		this.msgflag = errMsg.getMsgflag();
	}

	@Override
	public String toString() {
		return this.errMessage;
	}

	public int getErrCode() {
		return status;
	}

	public int getMsgflag() {
		return msgflag;
	}
}
