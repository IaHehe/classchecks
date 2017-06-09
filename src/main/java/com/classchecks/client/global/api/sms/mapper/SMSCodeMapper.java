package com.classchecks.client.global.api.sms.mapper;

import org.apache.ibatis.annotations.Param;

public interface SMSCodeMapper {
	/**
	 * 
	* @Title: hasPhoneRegistered 
	* @Description: 查询手机号码是否注册
	* @param phone
	* @return
	* String 
	 */
	public String[] hasPhoneRegistered(@Param("phone")String phone);
	
}
