package com.classchecks.client.student.api.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.classchecks.client.student.api.login.service.LoginService;
import com.framework.basic.vo.BasicEntityVo;

@Controller
@RequestMapping("/student")
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	@RequestMapping("/login")
	@ResponseBody
	public BasicEntityVo<?> login(String phone, String smscode, boolean isVerifySmsCode, String regId) {
		
		return loginService.login(phone, smscode, isVerifySmsCode, regId);
	}

}
