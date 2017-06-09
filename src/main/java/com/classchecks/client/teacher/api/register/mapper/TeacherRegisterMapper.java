package com.classchecks.client.teacher.api.register.mapper;

import com.classchecks.client.student.api.register.vo.SecurityAccountVo;

/**
 * 
* @ClassName: TeacherRegisterMapper 
* @Description: 教师注册mapper
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年5月29日 下午2:41:39 
*
 */
public interface TeacherRegisterMapper {

	/**
	 * 
	* @Title: saveRegisterInfo 
	* @Description: 保存用户的注册信息 
	* @param secAcc
	* void 
	* @throws
	 */
	public void saveRegisterInfo(SecurityAccountVo secAcc);
	
}
