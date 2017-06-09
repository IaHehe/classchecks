package com.classchecks.client.teacher.api.clockin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.classchecks.client.teacher.api.clockin.vo.TeachingRosterVo;
import com.classchecks.common.basic.vo.SchoolCourseClockModel;

public interface ClockinAsTeacherMapper {

	/**
	 * 
	* @Title: findAll 
	* @Description: 以教师教务账号查询教学班所有学生名单
	* @param jwAccountAsTeahcer
	* @return
	* List<TeachingRosterVo> 
	 */
	public List<TeachingRosterVo> findAllAsRoster(@Param("scClockModel")SchoolCourseClockModel sccm, @Param("jwAccount")String jwAccountAsTeacher);
	
	/**
	 * 添加考勤记录
	 * 
	 * 教师人脸识别考勤后增加考勤记录
	 * 
	 * @param jwAccount
	 *            工号
	 * @param idCourseTime
	 *            课程上课时间ID
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @param schoolCourseClockModel
	 *            学校时钟
	 */
	public void insertClockInRecord(@Param("jwAccount") String jwAccount, @Param("IDCourseTime") Integer idCourseTime,
			@Param("lng") Double lng, @Param("lat") Double lat,
			@Param("schoolClock") SchoolCourseClockModel schoolCourseClockModel);

	/**
	 * 获得指定学校时钟 指定工号的课程时间ID
	 * 
	 * @param jwAccount
	 *            工号
	 * @param schoolCourseClockModel
	 *            学校时钟
	 * @return 课程时间ID
	 */
	public Integer getCourseTimeByTeacherNoNow(@Param("jwAccount") String jwAccount,
			@Param("schoolClock") SchoolCourseClockModel schoolCourseClockModel);

	/**
	 * 添加学生考勤记录
	 * 
	 * @param idCourseTime
	 *            课程时间ID
	 * @param week
	 *            周次
	 * @param studentList
	 *            学生账号列表
	 */
	public void insertStudentClockInRecord(@Param("idCourseTime") Integer idCourseTime, @Param("week") Integer week,
			@Param("stuList") List<TeachingRosterVo> studentList);
	
}
