package com.classchecks.client.teacher.api.clockin.service;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.client.teacher.api.clockin.vo.TeachingRosterVo;
import com.framework.basic.vo.BasicEntityListVo;

public interface ClockinAsTeacherService {

	/**
	 * 
	* @Title: clockin 
	* @Description: 教师拍照考勤 
	* @param jwAccount
	* @param phone
	* @param clockinImg
	* @return
	* BasicEntityListVo<?> 
	* @throws
	 */
	//public BasicEntityListVo<?> clockin(String jwAccount, String phone, Double longitude, Double latitude, CommonsMultipartFile clockinImg, int byWhat);
	public BasicEntityListVo<?> clockin(String jwAccount, String phone, Double longitude, Double latitude, CommonsMultipartFile clockinImg);
	
	
	/**
	 * 保存考勤记录
	 * 
	 * @param jwAccount
	 *            工号
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @param studentList
	 *            学生列表
	 */
	public boolean saveClockInRecord(String jwAccount, Double lng, Double lat, List<TeachingRosterVo> rosters);
	
}
