/**
 * Copyright © 2017. by Tengyu Ma
 * CreateTime: 2017年5月22日 下午11:17:29 
 * @author  Tengyu Ma  mty2015@126.com 
 * @version V1.0.0
 */
package com.classchecks.common.basic.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.classchecks.common.basic.vo.SchoolCourseTimeVo;


/**
 * 学校时间Mapper
 * 
 * @author matengyu
 */
public interface SchoolDateTimeMapper {

	/**
	 * 获取指定学年学期的第一天日期
	 * 
	 * @param cyear
	 *            学年
	 * @param csemester
	 *            学期
	 */
	Date getFirstDate(@Param("cyear") int cyear, @Param("csemester") int csemester);

	/**
	 * 获取上课时间表
	 * 
	 * @return
	 */
	List<SchoolCourseTimeVo> getAllSchoolCourseTime();

}
