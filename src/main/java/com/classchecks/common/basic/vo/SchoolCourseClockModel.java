/**
 * Copyright © 2017. by Tengyu Ma
 * CreateTime: 2017年5月31日 上午1:21:18 
 * @author  Tengyu Ma  mty2015@126.com 
 * @version V1.0.0
 */
package com.classchecks.common.basic.vo;

/**
 * 学校上课时钟 保存学年学期周次星期节次
 *
 * @author matengyu
 */
public class SchoolCourseClockModel {

	public SchoolCourseClockModel() {
	}

	public SchoolCourseClockModel(Integer cyear, Integer csemester, Integer week, Integer weekday, Integer section) {
		this.cyear = cyear;
		this.csemester = csemester;
		this.week = week;
		this.weekday = weekday;
		this.section = section;
	}

	/**
	 * 学年
	 */
	private Integer cyear;
	/**
	 * 学期
	 */
	private Integer csemester;
	/**
	 * 周次
	 */
	private Integer week;
	/**
	 * 星期
	 */
	private Integer weekday;
	/**
	 * 节次
	 */
	private Integer section;

	/**
	 * @return the cyear
	 */
	public Integer getCyear() {
		return cyear;
	}

	/**
	 * @param cyear
	 *            the cyear to set
	 */
	public void setCyear(Integer cyear) {
		this.cyear = cyear;
	}

	/**
	 * @return the csemester
	 */
	public Integer getCsemester() {
		return csemester;
	}

	/**
	 * @param csemester
	 *            the csemester to set
	 */
	public void setCsemester(Integer csemester) {
		this.csemester = csemester;
	}

	/**
	 * @return the week
	 */
	public Integer getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            the week to set
	 */
	public void setWeek(Integer week) {
		this.week = week;
	}

	/**
	 * @return the weekday
	 */
	public Integer getWeekday() {
		return weekday;
	}

	/**
	 * @param weekday
	 *            the weekday to set
	 */
	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}

	/**
	 * @return the section
	 */
	public Integer getSection() {
		return section;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(Integer section) {
		this.section = section;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SchoolCourseClockModel [cyear=" + cyear + ", csemester=" + csemester + ", week=" + week + ", weekday="
				+ weekday + ", section=" + section + "]";
	}
}
