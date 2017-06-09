package com.classchecks.client.student.api.login.service;

import com.framework.basic.vo.BasicEntityVo;

public interface LoginService {

	/**
	 * 
	* @Title: login 
	* @Description: 用户登录
	* @param phone 手机号码
	* @param smscode 短信验证码
	* @param isAutoLogin 是否验证短信验证码
	* @return
	* BasicEntityVo 
	 */
	public BasicEntityVo<?> login(String phone, String smscode, boolean isVerifySmsCode, String regId);
	
}
