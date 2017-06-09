package com.classchecks.client.teacher.api.login.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.classchecks.client.global.api.sms.mapper.SMSCodeMapper;
import com.classchecks.client.teacher.api.login.mapper.TeacherLoginMapper;
import com.classchecks.client.teacher.api.login.service.TeacherLoginService;
import com.classchecks.client.teacher.api.login.vo.TeacherVo;
import com.framework.basic.vo.BasicEntityVo;
import com.framework.common.sms.bmob.SMSUtil;
import com.framework.content.code.LoginBusinessCode;


@Service
public class TeacherLoginServiceImpl implements TeacherLoginService {
	private static Logger LOG = LoggerFactory.getLogger(TeacherLoginServiceImpl.class);

	@Autowired
	private TeacherLoginMapper teacherLoginMapper;
	@Autowired
	private SMSCodeMapper smsCodeMapper;
	
	/*  
	* <p>Title: login</p> 
	* <p>Description: 教师登录</p> 
	* @param phone
	* @param smscode
	* @param isAutoLogin 表示是否要调用短信验证接口，用户首次登录为true, 客户端自动登录时去数据库验证就行
	* @return 
	* @see com.classchecks.client.student.api.login.service.LoginService#login(java.lang.String, java.lang.String, boolean) 
	*/ 
	@Override
	@Transactional(readOnly=false, rollbackFor=RuntimeException.class)
	public BasicEntityVo<TeacherVo> login(String phone, String smscode, boolean isVerifySmsCode, String regId) {
		
		// 检测数据库是否已有记录，这里检查是防止用户获取验证码成功后，跟换一个没有的手机号来登录
		boolean hasPhone = smsCodeMapper.hasPhoneRegistered(phone).length > 0 ? true : false;
		if(!hasPhone) {
			return new BasicEntityVo<>(LoginBusinessCode.BUSSINESS_NOT_EXIST[0], LoginBusinessCode.BUSSINESS_NOT_EXIST[1]);
		}
		LOG.info("isVerifySmsCode="+isVerifySmsCode);
		// 调用短信接口验证短信验证码是否匹配
		if(isVerifySmsCode) {
			// 调用短信接口验证输入的短信验证码是否可用
			boolean isVerify = SMSUtil.verifySmsCode(phone, smscode);
			
			if(!isVerify) { // 短信接口验证失败
				LOG.info("login()->短信接口验证失败...");
				return new BasicEntityVo<>(LoginBusinessCode.BUSSINESS_SMS_ERROR[0], LoginBusinessCode.BUSSINESS_SMS_ERROR[1]);
			} 
			try {
				// 更新数据库中用户的验证码，在客户端自动登录时作为临时密码
				teacherLoginMapper.updateBySecurityAccount(phone, smscode, regId);
				// 查询数据库中的用户信息
				TeacherVo sv = teacherLoginMapper.findStudentInfo(phone, smscode);
				return new BasicEntityVo<>(
						LoginBusinessCode.BUSINESS_SUCCESS[0], LoginBusinessCode.BUSINESS_SUCCESS[1], sv);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				LOG.error("login()->登录时更新数据验证码错误", e);
				return new BasicEntityVo<>(LoginBusinessCode.BUSSINESS_SQL_EXCEPTION[0], LoginBusinessCode.BUSSINESS_SQL_EXCEPTION[1]);
			}
		}
		
		// 查询数据库中的用户信息
		TeacherVo sv = teacherLoginMapper.findStudentInfo(phone, smscode);
		
		if(null == sv) { // 如果没有查询到，返回用户不存在
			return new BasicEntityVo<>(LoginBusinessCode.BUSSINESS_NOT_EXIST[0], LoginBusinessCode.BUSSINESS_NOT_EXIST[1]); 
		}
		
		return new BasicEntityVo<>(
				LoginBusinessCode.BUSINESS_SUCCESS[0], LoginBusinessCode.BUSINESS_SUCCESS[1], sv);
	}
	
	
}
