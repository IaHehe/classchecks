package com.classchecks.client.teacher.api.login.service;

import com.framework.basic.vo.BasicEntityVo;

public interface TeacherLoginService {

	/**
	 * 
	* @Title: login 
	* @Description: 教师登录
	* @param phone 手机号码
	* @param smscode 短信验证码
	* @param isAutoLogin 是否验证短信验证码
	* @return
	* BasicEntityVo 
	 */
	public BasicEntityVo<?> login(String phone, String smscode, boolean isVerifySmsCode, String regId);
	
}
