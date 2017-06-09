package com.classchecks.client.global.api.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.classchecks.client.global.api.sms.service.SMSCodeService;
import com.framework.basic.vo.BasicVo;

@Controller
@RequestMapping("/global/sms")
public class SMSCodeController {

	@Autowired
	private SMSCodeService smsCodeService;
	
	/**
	 * 
	* @Title: requestSMSCode 
	* @Description: 注册请求短信验证码
	* @param phone
	* @return
	* String 
	 */
	@RequestMapping("/register-sms-code")
	@ResponseBody
	public BasicVo doRegisterSMSCode(String phone) {
		return smsCodeService.registerSMSCode(phone);
	}
	
	/**
	 * 
	* @Title: doLoginSMSCode 
	* @Description: 登录请求发送验证码 
	* @param phone
	* @return
	* BasicVo
	 */
	@RequestMapping("/login-sms-code")
	@ResponseBody
	public BasicVo doLoginSMSCode(String phone) {
		return smsCodeService.loginSMSCode(phone);
	}
	
}
