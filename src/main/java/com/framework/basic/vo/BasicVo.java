package com.framework.basic.vo;

public class BasicVo {

	private String code;
	private String message;

	public BasicVo() {
	}

	public BasicVo(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BasicVo [code=" + code + ", message=" + message + "]";
	}

}
