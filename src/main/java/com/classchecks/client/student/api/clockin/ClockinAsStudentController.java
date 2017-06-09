package com.classchecks.client.student.api.clockin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.client.student.api.clockin.service.ClockinAsStudentService;
import com.framework.basic.vo.BasicEntityVo;
import com.framework.common.util.StringUtils;
import com.framework.content.code.StudentClockInBusinessCode;

@Controller
@RequestMapping("/student")
public class ClockinAsStudentController {

	@Autowired
	private ClockinAsStudentService clockinAsStudentService;
	
	/**
	 * 
	* @Title: doClockIn 
	* @Description: 学生在教师拍照考勤时，没有检测到自己考勤
	* @return
	* BasicEntityVo<?>
	 */
	@RequestMapping("/clock-in")
	@ResponseBody
	public BasicEntityVo<?> doClockIn(String jwAccount, String loginAccount, 
			Double lng, Double lat, @RequestParam("clockinImg")CommonsMultipartFile file) {
		if(StringUtils.isBlank(jwAccount)) {
			return new BasicEntityVo<>(StudentClockInBusinessCode.JW_ACCOUNT_EMPTY[0], StudentClockInBusinessCode.JW_ACCOUNT_EMPTY[1]);
		}
		if(StringUtils.isBlank(loginAccount)) {
			return new BasicEntityVo<>(StudentClockInBusinessCode.LOGIN_ACCOUNT_EMPTY[0], StudentClockInBusinessCode.LOGIN_ACCOUNT_EMPTY[1]);
		}
		if(lng == 0.0 || null == lng || lat == 0.0 || null == lat) {
			return new BasicEntityVo<>(StudentClockInBusinessCode.LNG_LAT_EMPTY[0], StudentClockInBusinessCode.LNG_LAT_EMPTY[1]);
		}
		if(file.isEmpty()) {
			return new BasicEntityVo<>(StudentClockInBusinessCode.BUSSINESS_IMAGE_EMPTY[0], StudentClockInBusinessCode.BUSSINESS_IMAGE_EMPTY[1]);
		}
		
		return clockinAsStudentService.clockin(jwAccount, loginAccount, lng, lat, file);
	}

}
