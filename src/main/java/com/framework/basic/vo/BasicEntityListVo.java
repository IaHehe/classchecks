package com.framework.basic.vo;

import java.util.List;

public class BasicEntityListVo<T> {
	private String code;
	private String message;
	private List<T> dataList;

	public BasicEntityListVo() {
	}

	public BasicEntityListVo(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public BasicEntityListVo(String code, String message, List<T> dataList) {
		this.code = code;
		this.message = message;
		this.dataList = dataList;
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

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

}
