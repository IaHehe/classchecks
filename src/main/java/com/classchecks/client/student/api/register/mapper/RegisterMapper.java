package com.classchecks.client.student.api.register.mapper;

import com.classchecks.client.student.api.register.vo.SecurityAccountVo;

public interface RegisterMapper {
	
	/**
	 * 
	* @Title: saveRegisterInfo 
	* @Description: 保存用户的注册信息 
	* @param secAcc
	* void 
	* @throws
	 */
	public void saveRegisterInfo(SecurityAccountVo secAcc);
	
	/**
	 * 
	* @Title: findAccountByPhone 
	* @Description: 查询用户注册信息，以注册的手机号
	* @param phone
	* @return
	* SecurityAccountVo 
	* @throws
	 */
	public SecurityAccountVo findAccountByPhone(String phone);
	
}
