package com.framework.content.code;

public class SMSCodeBusinessCode {
	
	
	/**
	 * 请求成功返回300:验证码获取成功
	 */
	public final static String [] BUSINESS_SUCCESS = {"3000", "验证码获取成功"};
	
	public final static String [] BUSSINESS_FAILED = {"3001", "手机号不存在或间隔时间短"};
	
	public final static String [] BUSSINESS_ERROR = {"3002", "获取验证错误"};
	
	public final static String [] BUSSINESS_EXIST = {"3003", "手机号已注册"};
	
	public final static String [] BUSSINESS_NOT_EXIST = {"3004", "手机号不存在，请先注册"};
}
