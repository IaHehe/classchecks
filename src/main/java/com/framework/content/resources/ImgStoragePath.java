package com.framework.content.resources;

public class ImgStoragePath {
	
	
	// 远程服务器端与本地服务器存储文件的根路径
	private final static String SERVER = "C:\\";
	private final static String LOCAL = "E:\\";
	
	private final static String STORAGE_LOCATION = SERVER;
	
	// =====================================【人脸收集保存位置配置】=======================================
	// 用户上传的人脸图片临时存储位置
	public final static String RAW_FACE_IMG_SAVE_PATH = STORAGE_LOCATION + "classchecks\\image\\raw";
	// 用户人脸信息经过预处理后的保存位置
	public final static String PROC_FACE_IMG_SAVE_PATH = STORAGE_LOCATION + "classchecks\\image\\proc";
	// 用户人脸信息生成的CSV标签文件保存路径
	public final static String CSV_FILE_SAVE_PATH = STORAGE_LOCATION + "classchecks\\csv_face.txt";
	public final static String IMAGE_FILE_TEMP  = STORAGE_LOCATION + "classchecks\\image\\temp";
	
	// =====================================【教师端拍照考勤图片保存位置配置】=======================================
	public final static String TEACHER_CLOCK_IN_IMG_PATH = STORAGE_LOCATION + "classchecks\\image\\teacher";
	
	// =====================================【学生端自己签到考勤图片保存位置配置】=======================================
	public final static String STUDENT_CLOCK_IN_IMG_PATH = STORAGE_LOCATION + "classchecks\\image\\student";
}
