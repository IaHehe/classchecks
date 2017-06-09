package com.framework.context;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

public class ServletContextHolder implements ServletContextAware {
	private static Logger Log = LoggerFactory.getLogger(ServletContextHolder.class);
	
	private static ServletContext mServletContext;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		ServletContextHolder.mServletContext = servletContext;
	}
	
	public static ServletContext getServletContext() {
		checkServletContext();
		return mServletContext;
	}
	
	/**
	 * 
	* @Title: getOpenCVDLLPath 
	* @Description: 获取OpenCV动态链接库在服务器的绝对路径
	* @return
	* String 
	 */
	public static String getOpenCVDLLPath() {
		checkServletContext(); 
		String dllAbsolutePath = mServletContext.getRealPath("WEB-INF/dll/x64/opencv_java320.dll");
		Log.info("dllAbsolutePath="+dllAbsolutePath);
		return dllAbsolutePath;
	}
	
	/**
	 * 
	* @Title: getOpenCVHaarcascades 
	* @Description: 获取OpenCV人脸、人眼检测器XML文件绝对路径
	* @return
	* String 
	 */
	public static String getOpenCVHaarcascades() {
		checkServletContext();
		String haarcascades = mServletContext.getRealPath("WEB-INF/classes/haarcascades/");
		Log.info("haarcascades=" + haarcascades);
		return haarcascades;
	}
	
	private static void checkServletContext() {
		if (mServletContext == null) {
			throw new IllegalStateException("mServletContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}

}
