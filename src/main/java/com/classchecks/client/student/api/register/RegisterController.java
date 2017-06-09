package com.classchecks.client.student.api.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.client.student.api.register.service.RegisterService;
import com.framework.basic.vo.BasicVo;

@Controller
@RequestMapping("/student")
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	
	/**
	 * 
	* @Title: register 
	* @Description: 注册
	* @param phone
	* @param smscode
	* @param regID  极光推送从客户端获取的唯一标识
	* @param files
	* @return
	* BasicVo 
	* @throws
	 */
	@RequestMapping("/register")
	@ResponseBody
	public BasicVo register(String phone, String smscode, String regID, @RequestParam("files")CommonsMultipartFile[] files) {
		return registerService.register(phone, smscode, regID, files);
	}

}
