/**
 * Copyright © 2017. by Tengyu Ma
 * CreateTime: 2017年5月22日 下午9:46:45 
 * @author  Tengyu Ma  mty2015@126.com 
 * @version V1.0.0
 */
package com.classchecks.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 学校时间践转换类
 * 
 * @author matengyu
 */
public class SchoolDateTimeUtil {

	/**
	 * 获取现在的周次
	 * 
	 * @param firstWeekDate
	 *            第一周第一天的日期
	 * @return 现在的周次
	 */
	public Integer getCurrentWeek(Date firstWeekDate) {
		long time1 = firstWeekDate.getTime();
		long time2 = new Date().getTime();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		Integer days = Integer.parseInt(String.valueOf(between_days)) + 1;
		return (days + 6) / 7;
	}

	/**
	 * 获取现在的星期
	 * 
	 * 1---星期一 7---星期日
	 * 
	 * @return 现在的星期
	 */
	public Integer getCurrentWeekDay() {
		Integer weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		if (weekday == 0) {
			weekday = 7;
		}
		return weekday;

	}

	/**
	 * Description: 获取当前学期 1,2 <br/>
	 * 定义3-7为第二学期其他为第一学期
	 * 
	 * @return
	 */
	public String getCurrentSemester() {
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.MONTH) >= 2 && c.get(Calendar.MONTH) <= 6) {
			return "2";
		} else {
			return "1";
		}
	}

	/**
	 * Description: 获取当前学期 1,2 <br/>
	 * 定义3-7为第二学期其他为第一学期
	 * 
	 * @return
	 */
	public Integer getCurrentSemesterInteger() {
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.MONTH) >= 2 && c.get(Calendar.MONTH) <= 6) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * Description: 获取当前学年
	 * 
	 * @return
	 */
	public String getCurrentSchoolYear() {
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.MONTH) >= 7) {
			return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.YEAR) + 1);
		} else {
			return (c.get(Calendar.YEAR) - 1) + "-" + c.get(Calendar.YEAR);
		}
	}

	/**
	 * 获取年份。学年起始年
	 * 
	 * @return
	 */
	public Integer getCurrentYear() {
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.MONTH) >= 7) {
			return Calendar.getInstance().get(Calendar.YEAR);
		} else {
			return Calendar.getInstance().get(Calendar.YEAR) - 1;
		}
	}
}
