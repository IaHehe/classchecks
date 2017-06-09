package com.framework.common.sms.netease;

import java.util.HashMap;
import java.util.Map;

import com.framework.common.util.properties.PropertiesUtil;

public class SMSUtils {
	
	// 发送短信验证码URL
	public final static String NETEASE_SMS_SEND_CODE_URL = "https://api.netease.im/sms/sendcode.action";
	// 校验验证码URL
	public final static String NETEASE_SMS_VERIFY_CODE_URL = "https://api.netease.im/sms/verifycode.action";
	
	private static String appKey = "";
	private static String appSecret = "";
	private static int templateId = 0;
	private static int codeLen = 0;
	
	static {
		PropertiesUtil.setPropertiesFile("config/sms/netease.properties");
    	appKey = PropertiesUtil.getProperty("AppKey");
    	appSecret = PropertiesUtil.getProperty("AppSecret");
    	templateId = Integer.parseInt(PropertiesUtil.getProperty("TemplateId"));
    	codeLen = Integer.parseInt(PropertiesUtil.getProperty("CodeLength"));
	}
	
	public static String sendCode(Map<String, String> reqParams) {
		NeteaseUtils.initAppInfo(appKey, appSecret, templateId, codeLen);
		return NeteaseUtils.post(reqParams, NETEASE_SMS_SEND_CODE_URL);
	}
	
	public static String verifyCode(Map<String, String> reqParams) {
		NeteaseUtils.initAppInfo(appKey, appSecret, templateId, codeLen);
		return NeteaseUtils.post(reqParams, NETEASE_SMS_VERIFY_CODE_URL);
	}
    public static void main(String[] args) throws Exception{
    	
    	
//    	Map<String, String> queryParams = new HashMap<>();
//    	queryParams.put("mobile", "18302390780");
//    	String s = sendCode(queryParams);
//    	System.out.println(s);
    	
    	// // verify failed: {"code":404,"msg":"verify null","obj":3}
    	Map<String, String> queryParams = new HashMap<>();
    	queryParams.put("mobile", "18302390780");
    	queryParams.put("code", "3518");
    	String s1 = verifyCode(queryParams);
    	System.out.println("s1:"+s1);
    }
}
