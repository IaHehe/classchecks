package com.classchecks.client.student.api.clockin.mapper;

import org.apache.ibatis.annotations.Param;

import com.classchecks.client.student.api.clockin.vo.PointVo;
import com.classchecks.common.basic.vo.SchoolCourseClockModel;

public interface ClockinAsStudentMapper {

	/**
	 * 
	* @Title: findStudentName 
	* @Description: 以教务账号查询学生姓名
	* @param loginAccount
	* @return
	* String 
	* @throws
	 */
	public String findStudentName(@Param("jwAccount")String loginAccount);
	
	/**
	 * 通过学生学号 获取 当前考勤的课程对应的 教师ID
	 * 
	 * @param jwAccount
	 *            学生学号
	 * @param studentTimeOut
	 *            学生考勤超时
	 * @return 教师ID
	 */
	public Integer getTeacherIDByStudentClock(@Param("jwAccount") String jwAccount,
			@Param("studentTimeOut") Integer studentTimeOut);
	
	/**
	 * 通过教师ID 学校时钟获得经纬度坐标
	 * 
	 * @param teacherID
	 *            教师ID
	 * @param schoolCourseClockModel
	 *            学校时钟
	 */
	public PointVo getGPSByTeacherID(@Param("teacherID") Integer teacherID,
			@Param("schoolClock") SchoolCourseClockModel schoolCourseClockModel);
	
	/**
	 * 
	* @Title: updateStudentClockinRecord 
	* @Description: 在学生自己考勤成功后更新学生考勤记录
	* @param jwAccount
	* void 
	* @throws
	 */
	public void updateStudentClockinRecord(@Param("jwAccount")String jwAccount);
	
}
