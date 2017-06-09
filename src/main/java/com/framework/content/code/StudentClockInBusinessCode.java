package com.framework.content.code;

public class StudentClockInBusinessCode {

	/**
	 * 学生考勤返回码
	 */
	public final static String [] BUSINESS_SUCCESS = {"5000", "签到成功"};
	
	public final static String [] BUSSINESS_SQL_EXCEPTION = {"5001", "数据库异常"};
	
	public final static String [] BUSSINESS_NO_DETECT_FACE = {"5002", "没有检测到人脸，请重试"};
	
	public final static String [] BUSSINESS_IMAGE_SAVE_FAILED = {"5003", "上传的图片保存失败"};
	
	public final static String [] BUSSINESS_IMAGE_EMPTY = {"5004", "上传的图片为空"};
	
	public final static String [] JW_ACCOUNT_EMPTY = {"5005", "教务账号空"};
	
	public final static String [] LOGIN_ACCOUNT_EMPTY = {"5006", "登录账号空"};
	
	public final static String [] LNG_LAT_EMPTY = {"5007", "经纬度空"};
	
	public final static String [] GPS_DISTANCE_GT_50 = {"5008", "与教师的距离太远，不能考勤"};
	public final static String [] FACE_NON_EXISTENT = {"5009", "检测的人脸不存在，请重试"};

}
