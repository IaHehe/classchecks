package com.classchecks.client.teacher.api.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.classchecks.client.teacher.api.register.service.TeacherRegisterService;
import com.framework.basic.vo.BasicVo;

@Controller
@RequestMapping("/teacher")
public class TeacherRegisterController {

	@Autowired
	private TeacherRegisterService teacherRegisterService;
	
	@RequestMapping("/register")
	@ResponseBody
	public BasicVo doRegister(String phone, String smscode, String regID) {
		return teacherRegisterService.teacherRegister(phone, smscode, regID);
	}
	
}
