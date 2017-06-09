package com.classchecks.client.global.api.jpush.vo;

public class JPushByTeacherVo {

	/**
	 * 教师姓名
	 */
	private String teacherName;
	/**
	 * 设备ID
	 */
	private String teacherRegId;
	/**
	 * 课程名字
	 */
	private String courseName;

	/**
	 * 上课节次
	 */
	private Integer startSection;
	/**
	 * 星期
	 */
	private Integer weekday;

	public JPushByTeacherVo() {

	}

	public JPushByTeacherVo(String teacherName, String teacherRegId, String courseName, Integer startSection,
			Integer weekday) {
		super();
		this.teacherName = teacherName;
		this.teacherRegId = teacherRegId;
		this.courseName = courseName;
		this.startSection = startSection;
		this.weekday = weekday;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherRegId() {
		return teacherRegId;
	}

	public void setTeacherRegId(String teacherRegId) {
		this.teacherRegId = teacherRegId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getStartSection() {
		return startSection;
	}

	public void setStartSection(Integer startSection) {
		this.startSection = startSection;
	}

	public Integer getWeekday() {
		return weekday;
	}

	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}

	@Override
	public String toString() {
		return "JPushByTeacherVo [teacherName=" + teacherName + ", teacherRegId=" + teacherRegId + ", courseName="
				+ courseName + ", startSection=" + startSection + ", weekday=" + weekday + "]";
	}

}
