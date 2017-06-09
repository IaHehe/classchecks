package com.classchecks.client.teacher.api.clockin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.client.teacher.api.clockin.service.ClockinAsTeacherService;
import com.framework.basic.vo.BasicEntityListVo;
import com.framework.basic.vo.BasicVo;

@Controller
@RequestMapping("/teacher")
public class ClockinAsTeacherController {

	@Autowired
	private ClockinAsTeacherService clockinAsTeacherService;
	
	/**
	 * 
	* @Title: doClockIn 
	* @Description: 教师拍照上传对学生进行考勤
	* @return
	* BasicEntityListVo<?> 
	 */
	@RequestMapping("/clock-in")
	@ResponseBody
	public BasicEntityListVo<?> doClockIn(String jwAccount, String phone, Double longitude, Double latitude, @RequestParam("clockinImg")CommonsMultipartFile file, int byWhat) {
		return clockinAsTeacherService.clockin(jwAccount, phone, longitude, latitude, file);
	}

}
