package com.framework.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ContextHttpServletRequest {

	/**
	 * 
	* @Title: getRequest 
	* @Description: TODO(获取request对象) 
	* @return
	* HttpServletRequest 
	* @throws
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
}
