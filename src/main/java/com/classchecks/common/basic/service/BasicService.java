package com.classchecks.common.basic.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classchecks.common.basic.mapper.SchoolDateTimeMapper;
import com.classchecks.common.basic.vo.SchoolCourseClockModel;
import com.classchecks.common.basic.vo.SchoolCourseTimeVo;
import com.classchecks.common.util.SchoolDateTimeUtil;
import com.framework.common.util.StringUtils;


/**
 * 基本服务
 *
 * 包含学校时钟转换
 * 
 * @author matengyu
 */
@Service
public class BasicService {
	static final Logger LOG = LoggerFactory.getLogger(BasicService.class);

	@Autowired
	private SchoolDateTimeMapper schoolDateTimeMapper;

	/**
	 * 获取当前时间对应的学校时钟
	 * 
	 * @return
	 */
	public SchoolCourseClockModel getSchoolCourseClockModelNow() {
		SchoolDateTimeUtil schoolTime = new SchoolDateTimeUtil();
		int cyear = schoolTime.getCurrentYear();
		int csemester = Integer.parseInt(schoolTime.getCurrentSemester());
		int currentWeek = getCurrentWeek(cyear, csemester);
		int weekday = schoolTime.getCurrentWeekDay();

		int section = getSectionAttendanceTimeNow();

		SchoolCourseClockModel schoolCourseClockModel = new SchoolCourseClockModel(cyear, csemester, currentWeek,
				weekday, section);
		return schoolCourseClockModel;
	}

	/**
	 * 从数据库获取当前学期第一天的日期
	 * 
	 * @param cyear
	 * @param csemester
	 * @return
	 */
	private int getCurrentWeek(int cyear, int csemester) {
		Date firstDayDate = schoolDateTimeMapper.getFirstDate(cyear, csemester);
		// 计算获得现在周次
		SchoolDateTimeUtil schoolDateTimeUtil = new SchoolDateTimeUtil();
		return schoolDateTimeUtil.getCurrentWeek(firstDayDate);
	}

	/**
	 * 现在是否是考勤时间
	 * 
	 * @return true：是考勤时间 false：不是考勤时间
	 */
	public Boolean isAttendanceTimeNow() {
		// 从数据库获取每天的时间表
		// 前后扩展5分钟，然后计算现在是否在考勤时间内
		// List<SchoolCourseTime> list =
		// timeAttendanceMapper.getAllSchoolCourseTime();
		int x = getSectionAttendanceTimeNow();
		if (x == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 现在考勤的节次
	 * 
	 * @return 节次 -1 不是考勤时间
	 */
	public Integer getSectionAttendanceTimeNow() {
		// 从数据库获取每天的时间表
		// 前后扩展5分钟，然后计算现在是否在考勤时间内
		List<SchoolCourseTimeVo> list = schoolDateTimeMapper.getAllSchoolCourseTime();

		return getSection(list, Calendar.getInstance());
	}

	/**
	 * 计算指定时刻属于那一堂课
	 * 
	 * @param list
	 *            学校上课时间列表
	 * @param time
	 *            指定时刻
	 * @return 节次 出现异常或者不存在返回-1
	 */
	private Integer getSection(List<SchoolCourseTimeVo> list, Calendar time) {
		if (!StringUtils.notNull(list) || list.isEmpty() || !StringUtils.notNull(time)) {
			return -1;
		}
		boolean flag = false;
		Integer section = -1;

		Calendar tmpTime = null;
		Calendar tmpEndTime = null;
		Long startMilliSecond = 0L;
		Long endMilliSecond = 0L;
		Long timeMilliSecond = time.getTimeInMillis();

		int duringMilliSecond = 5 * 60 * 1000;

		for (SchoolCourseTimeVo schoolCourseTimeVo : list) {
			tmpTime = Calendar.getInstance();
			tmpEndTime = Calendar.getInstance();
			try {
				BeanUtils.copyProperties(tmpTime, time);
				tmpTime.set(Calendar.HOUR_OF_DAY, schoolCourseTimeVo.getHour());
				tmpTime.set(Calendar.MINUTE, schoolCourseTimeVo.getMiniute());
				tmpTime.set(Calendar.SECOND, 0);
				tmpEndTime.set(Calendar.HOUR_OF_DAY, schoolCourseTimeVo.getEndHour());
				tmpEndTime.set(Calendar.MINUTE, schoolCourseTimeVo.getEndMiniute());
				tmpEndTime.set(Calendar.SECOND, 0);

				startMilliSecond = tmpTime.getTimeInMillis() - duringMilliSecond;
				endMilliSecond = tmpEndTime.getTimeInMillis() + duringMilliSecond;
				if (timeMilliSecond > startMilliSecond && timeMilliSecond < endMilliSecond) {
					section = schoolCourseTimeVo.getSection();
					flag = true;
				}
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOG.debug("判断时间是哪一堂课时出现异常  SchoolCourseTimeVo:{0}", e);
				flag = true;
			}
			if (flag) {
				break;
			}
		}
		return section;
	}
}
