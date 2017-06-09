package com.classchecks.client.student.api.clockin.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.framework.basic.vo.BasicEntityVo;

public interface ClockinAsStudentService {
	
	/**
	 * 
	* @Title: clockin 
	* @Description: 学生考勤
	* @param jwAccount
	* @param loginAccount
	* @param lng
	* @param lat
	* @param file
	* @return
	* BasicEntityListVo<?> 
	* @throws
	 */
	public BasicEntityVo<?> clockin(String jwAccount, String loginAccount, Double lng, Double lat,
			CommonsMultipartFile file);

}
