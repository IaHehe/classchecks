package com.classchecks.client.teacher.api.register.service;

import com.framework.basic.vo.BasicVo;

public interface TeacherRegisterService {

	
	public BasicVo teacherRegister(String phone, String smscode, String regID);
	
}
