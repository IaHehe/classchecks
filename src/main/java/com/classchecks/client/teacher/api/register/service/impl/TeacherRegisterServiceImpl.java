package com.classchecks.client.teacher.api.register.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.classchecks.client.global.api.sms.mapper.SMSCodeMapper;
import com.classchecks.client.student.api.register.vo.SecurityAccountVo;
import com.classchecks.client.teacher.api.register.mapper.TeacherRegisterMapper;
import com.classchecks.client.teacher.api.register.service.TeacherRegisterService;
import com.framework.basic.vo.BasicVo;
import com.framework.common.sms.bmob.SMSUtil;
import com.framework.content.code.RegisterBusinessCode;


@Service
public class TeacherRegisterServiceImpl implements TeacherRegisterService{
	private static Logger LOG = LoggerFactory.getLogger(TeacherRegisterServiceImpl.class);
	
	private final static int Teacher_User_Type = 2; // 教师用户类型
	
	@Autowired
	private SMSCodeMapper smsCodeMapper;
	@Autowired
	private TeacherRegisterMapper teacherRegisterMapper;
	
	@Override
	@Transactional(readOnly=false, rollbackFor = RuntimeException.class)
	public BasicVo teacherRegister(String phone, String smscode, String regID) {
		// 检测数据库是否已有记录，这里检查是防止用户获取验证码成功后，更换一个已有的手机号输入
		boolean hasPhone = smsCodeMapper.hasPhoneRegistered(phone).length > 0 ? true : false;
		if(hasPhone) {
			return new BasicVo(RegisterBusinessCode.BUSSINESS_PHONE_EXIST[0], RegisterBusinessCode.BUSSINESS_PHONE_EXIST[1]);
		}
		
		// 调用短信接口验证输入的短信验证码是否可用
		boolean isVerify = SMSUtil.verifySmsCode(phone, smscode);
		
		if(!isVerify) { // 当短信验证失败后直接返回‘验证码错误’消息
			return new BasicVo(RegisterBusinessCode.BUSSINESS_SMS_ERROR[0], RegisterBusinessCode.BUSSINESS_SMS_ERROR[1]);
		}
		
		BasicVo basicVo = null;
		try {
			SecurityAccountVo secAcc = new SecurityAccountVo();
			secAcc.setSecurityAccount(phone);
			secAcc.setSecuritSmsCode(smscode);
			secAcc.setRegID(regID);
			secAcc.setSecuritType(Teacher_User_Type);
//			// 插入数据
			teacherRegisterMapper.saveRegisterInfo(secAcc);
			basicVo = new BasicVo(RegisterBusinessCode.BUSINESS_SUCCESS[0], RegisterBusinessCode.BUSINESS_SUCCESS[1]);
		} catch(Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			basicVo = new BasicVo(RegisterBusinessCode.BUSSINESS_FAILED[0], RegisterBusinessCode.BUSSINESS_FAILED[1]);
			LOG.error("教师注册错误", e);
		}
		return basicVo;
	}

}
