package com.classchecks.client.student.api.clockin.vo;

public class StudentClockinVo {

	private String stuName;
	private String curTime;

	public StudentClockinVo() {

	}

	public StudentClockinVo(String stuName, String curTime) {
		super();
		this.stuName = stuName;
		this.curTime = curTime;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getCurTime() {
		return curTime;
	}

	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}

	@Override
	public String toString() {
		return "StudentClockinVo [stuName=" + stuName + ", curTime=" + curTime + "]";
	}

}
