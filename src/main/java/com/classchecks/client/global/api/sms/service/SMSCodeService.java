package com.classchecks.client.global.api.sms.service;

import com.framework.basic.vo.BasicVo;

public interface SMSCodeService {

	/** 
	* @Title: registerSMSCode 
	* @Description: 注册请求短信验证码 
	* @param phone
	* @return
	* BasicVo  
	*/ 
	public BasicVo registerSMSCode(String phone);
	
	
	/** 
	* @Title: loginSMSCode 
	* @Description: 登录请求短信验证码
	* @param phone
	* @return
	* BasicVo 
	*/ 
	public BasicVo loginSMSCode(String phone);
	
}
