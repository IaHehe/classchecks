package com.classchecks.client.student.api.register.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.framework.basic.vo.BasicVo;

public interface RegisterService {

	/**
	 * 
	* @Title: register 
	* @Description: 用户注册
	* @param phone 手机号码
	* @param smscode 短信验证码
	* @param files 用户注册时拍摄的脸部图片文件
	* @return
	* BasicVo 
	 */
	public BasicVo register(String phone, String smscode, String regID, CommonsMultipartFile[] files);
	
}
