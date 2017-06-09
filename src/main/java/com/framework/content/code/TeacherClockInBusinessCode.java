package com.framework.content.code;

public class TeacherClockInBusinessCode {

	/**
	 * 教师考勤返回码
	 */
	public final static String [] BUSINESS_SUCCESS = {"4000", "考勤操作成功"};
	
	public final static String [] BUSSINESS_SQL_EXCEPTION = {"4001", "数据库异常"};
	
	public final static String [] BUSSINESS_NO_DETECT_FACE = {"4002", "没有检测到人脸，请重试"};
	
	public final static String [] BUSSINESS_IMAGE_SAVE_FAILED = {"4003", "上传的图片保存失败"};
	
	public final static String [] BUSSINESS_IMAGE_EMPTY = {"4004", "上传的图片为空"};
	
	public final static String [] BUSSINESS_NO_STU_LIST = {"4005", "没有查询到对应的学生名单"};
	
	public final static String [] JW_ACCOUNT_EMPTY = {"4006", "教务账号空"};
	
	public final static String [] LOGIN_ACCOUNT_EMPTY = {"4007", "登录账号空"};
	
	public final static String [] LNG_LAT_EMPTY = {"4008", "经纬度空"};

}
