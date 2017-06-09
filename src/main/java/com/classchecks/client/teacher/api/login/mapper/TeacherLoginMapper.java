package com.classchecks.client.teacher.api.login.mapper;

import org.apache.ibatis.annotations.Param;

import com.classchecks.client.teacher.api.login.vo.TeacherVo;

public interface TeacherLoginMapper {
	
	/**
	 * 
	* @Title: findStudentInfo 
	* @Description: 查询教师的基本信息 
	* @param phone
	* @param smscode
	* @return
	* String 
	 */
	public TeacherVo findStudentInfo(@Param("phone")String phone, @Param("smscode")String smscode);
	
	/**
	 * 
	* @Title: updateBySecurityAccount 
	* @Description: 更新短信验证码
	* @param phone
	* @param smscode
	* void 
	 */
	public void updateBySecurityAccount(@Param("phone")String phone, @Param("smscode")String smscode, @Param("regId")String regId);
	
}
