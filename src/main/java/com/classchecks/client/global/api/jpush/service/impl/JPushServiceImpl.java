package com.classchecks.client.global.api.jpush.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.classchecks.client.global.api.jpush.service.JPushService;
import com.classchecks.client.global.api.jpush.vo.JPushByTeacherVo;
import com.framework.basic.vo.BasicVo;
import com.framework.common.util.jpush.JPushUtils;
import com.framework.content.code.JPushBusinessCode;

import cn.jpush.api.push.PushResult;

@Service
public class JPushServiceImpl implements JPushService{


	/**
	 * 按regId推送通知给教师端
	 */
	@Override
	public BasicVo pushByRegId(List<JPushByTeacherVo> teachers) {
		
		if(teachers.size() == 0) {
			return new BasicVo(JPushBusinessCode.BUSSINESS_PUSH_EMPTY[0], JPushBusinessCode.BUSSINESS_PUSH_EMPTY[1]);
		}
		
		String pushAlert = "马上上课了，记得打开软件考勤哦!";
		String pushTitle = "考勤提醒";
		
		List<String> regIds = new ArrayList<String>();
		for(JPushByTeacherVo jt : teachers) {
			regIds.add(jt.getTeacherRegId());
		}
		PushResult pushResult = JPushUtils.sendPushByAndroidRegId(pushAlert, pushTitle, regIds);
		
		if(null == pushResult) {
			return new BasicVo(JPushBusinessCode.BUSSINESS_FAILED[0], JPushBusinessCode.BUSSINESS_FAILED[1]);
		}
		
		return new BasicVo(JPushBusinessCode.BUSINESS_SUCCESS[0], JPushBusinessCode.BUSINESS_SUCCESS[1]);
	}

	/**
	 * 推送通知给学生，按regID列表
	 */
	@Override
	public BasicVo pushStuClockByRegId(List<String> regIds) {
		if(regIds.size() == 0) {
			return new BasicVo(JPushBusinessCode.BUSSINESS_PUSH_EMPTY[0], JPushBusinessCode.BUSSINESS_PUSH_EMPTY[1]);
		}
		
		String pushAlert = "你当前教师考勤没有通过，请打开软件自己考勤";
		String pushTitle = "考勤提醒";
		
		PushResult pushResult = JPushUtils.sendPushByAndroidRegId(pushAlert, pushTitle, regIds);
		if(null == pushResult) {
			return new BasicVo(JPushBusinessCode.BUSSINESS_FAILED[0], JPushBusinessCode.BUSSINESS_FAILED[1]);
		}
		
		return new BasicVo(JPushBusinessCode.BUSINESS_SUCCESS[0], JPushBusinessCode.BUSINESS_SUCCESS[1]);
	}

}
