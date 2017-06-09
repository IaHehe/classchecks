/**
 * Copyright © 2017. by Tengyu Ma
 * CreateTime: 2017年5月17日 下午4:33:37 
 * @author  Tengyu Ma  mty2015@126.com 
 * @version V1.0.0
 */
package com.classchecks.common.basic.vo;

/**
 * 学校上课时间
 *
 * 包括上课小时和分钟
 * 
 * @author matengyu
 */
public class SchoolCourseTimeVo {
	/**
	 * 节次
	 */
	private Integer section;
	/**
	 * 小时
	 */
	private Integer hour;
	/**
	 * 分钟
	 */
	private Integer miniute;
	/**
	 * 下课时间 小时
	 */
	private Integer endHour;
	/**
	 * 下课时间 分钟
	 */
	private Integer endMiniute;

	/**
	 * @return the hour
	 */
	public Integer getHour() {
		return hour;
	}

	/**
	 * @param hour
	 *            the hour to set
	 */
	public void setHour(Integer hour) {
		this.hour = hour;
	}

	/**
	 * @return the miniute
	 */
	public Integer getMiniute() {
		return miniute;
	}

	/**
	 * @param miniute
	 *            the miniute to set
	 */
	public void setMiniute(Integer miniute) {
		this.miniute = miniute;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SchoolCourseTimeVo [section=" + section + ", hour=" + hour + ", miniute=" + miniute + ", endHour="
				+ endHour + ", endMiniute=" + endMiniute + "]";
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
	 * @return the endHour
	 */
	public Integer getEndHour() {
		return endHour;
	}

	/**
	 * @param endHour
	 *            the endHour to set
	 */
	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

	/**
	 * @return the endMiniute
	 */
	public Integer getEndMiniute() {
		return endMiniute;
	}

	/**
	 * @param endMiniute
	 *            the endMiniute to set
	 */
	public void setEndMiniute(Integer endMiniute) {
		this.endMiniute = endMiniute;
	}

}
