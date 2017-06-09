package com.classchecks.client.student.api.register.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.classchecks.client.student.api.register.mapper.RegisterMapper;
import com.classchecks.client.student.api.register.vo.SecurityAccountVo;

import junit.JUnitTestBase;

public class RegisterServiceTest extends JUnitTestBase {

	@Autowired
	private RegisterMapper registerMapper;
	
	@Autowired
	private RegisterService registerService;
	
	@Test
	//@Rollback(false)
	public void testRegister() {
		SecurityAccountVo vo = new SecurityAccountVo();
		vo.setSecurityAccount("13800000001");
		vo.setSecuritSmsCode("123456");
		vo.setSecuritType(1);
		registerService.register("13800000000", "1234", "", null);
		//registerMapper.saveRegisterInfo(vo);
	}
	
	@Test
	public void testfindAccountByPhone() {
		SecurityAccountVo vo = registerMapper.findAccountByPhone("testUserName1");
		System.out.println(vo);
	}

}
