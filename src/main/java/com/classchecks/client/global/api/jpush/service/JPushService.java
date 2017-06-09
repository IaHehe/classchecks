package com.classchecks.client.global.api.jpush.service;

import java.util.List;

import com.classchecks.client.global.api.jpush.vo.JPushByTeacherVo;
import com.framework.basic.vo.BasicVo;

public interface JPushService {

	
	public BasicVo pushByRegId(List<JPushByTeacherVo> teachers);
	
	public BasicVo pushStuClockByRegId(List<String> regIds);
	
}
