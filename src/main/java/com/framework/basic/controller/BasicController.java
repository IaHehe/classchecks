package com.framework.basic.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

/**
 * 
* @ClassName: BasicController 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年4月3日 下午11:29:18 
*
 */
public class BasicController {

	/**
	 * 
	* @Title: setRequestAttribute 
	* @Description: TODO(自动设置request对象) 
	* @param request Http请求对象
	* @param param  注意：该参数个数必须为双数，并且以键值对顺序排列，如: 'username','12345','phone','13100000000',其中'username','phone'为键
	* void 
	* @throws
	 */
	protected void setRequestAttribute(HttpServletRequest request, Object... param) {
		for(int i = 0; i < param.length; i ++) {
			if(null != param[i+1]) {
				request.setAttribute(param[i].toString(), param[i+1]);
			}
			i++;
		}
	}
	
	/**
	 * 
	* @Title: setModelAttribute 
	* @Description: TODO(自动设置Model对象) 
	* @param model
	* @param param 注意：该参数个数必须为双数，并且以键值对顺序排列，如: 'username','12345','phone','13100000000',其中'username','phone'为键
	* void 
	* @throws
	 */
	protected void setModelAttribute(Model model, Object... param) {
		for(int i = 0; i < param.length; i ++) {
			if(null != param[i + 1]) {
				model.addAttribute(param[i].toString(), param[i + 1]);
			}
			i ++;
		}
	}
	
}
