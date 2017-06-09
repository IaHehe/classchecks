package com.classchecks.client.global.api.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classchecks.client.global.api.sms.mapper.SMSCodeMapper;
import com.classchecks.client.global.api.sms.service.SMSCodeService;
import com.framework.basic.vo.BasicVo;
import com.framework.common.sms.bmob.SMSUtil;
import com.framework.content.code.SMSCodeBusinessCode;

import net.sf.json.JSONObject;

/** 
* @ClassName: SMSCodeServiceImpl 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年5月29日 上午2:24:26 
*  
*/ 
@Service
public class SMSCodeServiceImpl implements SMSCodeService{

	@Autowired
	private SMSCodeMapper smsCodeMapper;
	
	/**  
	* <p>Title: registerSMSCode</p> 
	* <p>Description: 注册请求验证码</p> 
	* @param phone
	* @return 
	* @see com.classchecks.client.global.api.sms.service.SMSCodeService#registerSMSCode(java.lang.String) 
	*/ 
	@Override
	public BasicVo registerSMSCode(String phone) {
		// 手机号码在系统不存在时，请求短信接口发送短信
		if(smsCodeMapper.hasPhoneRegistered(phone).length <= 0) {
			String smsResult = SMSUtil.sendSMS(phone);
//			// 形如"{\"msg\":\"ok\"}"的字符串
//			String jsonKey = (String) JSONObject.fromObject(smsResult).keys().next();
//			BasicVo basicVo;
//			if(SMSUtil.REQ_MSG_SUCCESS.equals(jsonKey)) {
//				basicVo = new BasicVo(SMSCodeBusinessCode.BUSINESS_SUCCESS[0], SMSCodeBusinessCode.BUSINESS_SUCCESS[1]);
//			} else if(SMSUtil.REQ_MSG_NOTFOUND.equals(jsonKey)) {
//				basicVo = new BasicVo(SMSCodeBusinessCode.BUSSINESS_FAILED[0], SMSCodeBusinessCode.BUSSINESS_FAILED[1]);
//			} else {
//				basicVo = new BasicVo(SMSCodeBusinessCode.BUSSINESS_ERROR[0], SMSCodeBusinessCode.BUSSINESS_FAILED[1]);
//			}
			return handleSMSResult(smsResult);
		}
		// 返回已经存在
		return new BasicVo(SMSCodeBusinessCode.BUSSINESS_EXIST[0], SMSCodeBusinessCode.BUSSINESS_EXIST[1]);
	}

	
	/** 
	* <p>Title: loginSMSCode</p> 
	* <p>Description: 登录请求短信验证码</p> 
	* @param phone
	* @return 
	* @see com.classchecks.client.global.api.sms.service.SMSCodeService#loginSMSCode(java.lang.String) 
	*/ 
	@Override
	public BasicVo loginSMSCode(String phone) {
		if(smsCodeMapper.hasPhoneRegistered(phone).length <= 0) {
			// 返回不存在，提示用户注册 
			return new BasicVo(SMSCodeBusinessCode.BUSSINESS_NOT_EXIST[0], SMSCodeBusinessCode.BUSSINESS_NOT_EXIST[1]);
		}
		String smsResult = SMSUtil.sendSMS(phone);
		
		return handleSMSResult(smsResult);
	}

	/**
	 * 
	* @Title: handleSMSResult 
	* @Description: 处理发送sms返回的结果 
	* @param smsResult
	* @return
	* BasicVo 
	 */
	private BasicVo handleSMSResult(String smsResult) {
		// 形如"{\"msg\":\"ok\"}"的字符串
		// 由于返回的字符串前面的key都不一样，需要单独处理
		String jsonKey = (String) JSONObject.fromObject(smsResult).keys().next();
		BasicVo basicVo;
		if(SMSUtil.REQ_MSG_SUCCESS.equals(jsonKey)) {
			basicVo = new BasicVo(SMSCodeBusinessCode.BUSINESS_SUCCESS[0], SMSCodeBusinessCode.BUSINESS_SUCCESS[1]);
		} else if(SMSUtil.REQ_MSG_NOTFOUND.equals(jsonKey)) {
			basicVo = new BasicVo(SMSCodeBusinessCode.BUSSINESS_FAILED[0], SMSCodeBusinessCode.BUSSINESS_FAILED[1]);
		} else {
			basicVo = new BasicVo(SMSCodeBusinessCode.BUSSINESS_ERROR[0], SMSCodeBusinessCode.BUSSINESS_FAILED[1]);
		}
		return basicVo;
	}
	
}
