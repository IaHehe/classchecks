package com.classchecks.client.global.api.jpush;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.classchecks.client.global.api.jpush.service.JPushService;
import com.classchecks.client.global.api.jpush.vo.JPushByTeacherVo;
import com.framework.basic.vo.BasicVo;
import com.framework.common.util.GsonUtil;

@Controller
@RequestMapping("/jpush")
public class JPushController {

	@Autowired
	private JPushService jpushService;
	
	@RequestMapping("/teacher/reg-id") 
	@ResponseBody
	public BasicVo doPushAsTeahcerRegId(String jsonString) {
		List<JPushByTeacherVo> vo = GsonUtil.jsonToList(jsonString, JPushByTeacherVo.class);
		return jpushService.pushByRegId(vo);
	}
	

}
