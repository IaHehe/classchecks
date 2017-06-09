package com.classchecks.client.student.api.login.mapper;

import org.apache.ibatis.annotations.Param;

import com.classchecks.client.student.api.login.vo.StudentVo;

public interface LoginMapper {
	
	/**
	 * 
	* @Title: findStudentInfo 
	* @Description: 查询学生的基本信息 
	* @param phone
	* @param smscode
	* @return
	* String 
	 */
	public StudentVo findStudentInfo(@Param("phone")String phone, @Param("smscode")String smscode);
	
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
