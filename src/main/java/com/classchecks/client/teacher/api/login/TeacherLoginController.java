package com.classchecks.client.teacher.api.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.classchecks.client.teacher.api.login.service.TeacherLoginService;
import com.framework.basic.vo.BasicEntityVo;

@Controller
@RequestMapping("/teacher")
public class TeacherLoginController {

	@Autowired
	private TeacherLoginService teacherLoginService;
	
	
	@RequestMapping("/login")
	@ResponseBody
	public BasicEntityVo<?> doLogin(String phone, String smscode, boolean isVerifySmsCode, String regId) {
		return teacherLoginService.login(phone, smscode, isVerifySmsCode, regId);
	}

}
