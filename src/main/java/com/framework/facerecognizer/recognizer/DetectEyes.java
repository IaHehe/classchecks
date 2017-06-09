package com.framework.facerecognizer.recognizer;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import com.framework.context.ServletContextHolder;


public class DetectEyes {
	private final static String HAARCASCADE_EYE = "haarcascade_eye.xml";
	private final static String HAARCASCADE_EYE_TREE_EYEGLASSES = "haarcascade_eye_tree_eyeglasses.xml";
	private final static String HAARCASCADE_MCS_LEFTEYE = "haarcascade_mcs_lefteye.xml";
	private final static String HAARCASCADE_MCS_RIGHTEYE = "haarcascade_mcs_righteye.xml";
	private static String DetectorXMLPath ;
	
	static {
//		DetectorXMLPath = Thread.currentThread().getClass().getResource("/").getPath().substring(1)
//				+"haarcascades/";
		DetectorXMLPath = ServletContextHolder.getOpenCVHaarcascades();
	}
	
	/**
	 * 检测左眼
	 * @param faceImg
	 * @param leftEyePoint
	 * @param searchedLeftEye
	 * @param scaledWidth
	 */
	protected static void detectLeftEye(Mat faceImg, Rect leftEyeRect, int scaledWidth) {
		CascadeClassifier cascade = new CascadeClassifier();
		cascade.load(DetectorXMLPath + HAARCASCADE_MCS_LEFTEYE);
		// 第一次检测左眼
		if(!cascade.empty()) {
			DetectObject.detectLargestObject(faceImg, cascade, leftEyeRect, scaledWidth);
		}
		// 如果没有检测到眼睛，使用不同的级联加载器继续尝试
		if(leftEyeRect.width <= 0) {
			cascade.load(DetectorXMLPath+HAARCASCADE_EYE_TREE_EYEGLASSES);
			if(!cascade.empty()) {
				DetectObject.detectLargestObject(faceImg, cascade, leftEyeRect, scaledWidth);
			}
		}
		if(leftEyeRect.width <= 0) {
			cascade.load(DetectorXMLPath+HAARCASCADE_EYE);
			if(!cascade.empty()) {
				DetectObject.detectLargestObject(faceImg, cascade, leftEyeRect, scaledWidth);
			}
		}
	}   
	
	protected static void detectRightEye(Mat faceImg, Rect rightEyeRect, int scaledWidth) {
		CascadeClassifier cascade = new CascadeClassifier();
		cascade.load(DetectorXMLPath+HAARCASCADE_MCS_RIGHTEYE);
		// 第一次检测右眼
		if(!cascade.empty()) {
			DetectObject.detectLargestObject(faceImg, cascade, rightEyeRect, scaledWidth);
		}
		if(rightEyeRect.width <= 0) {
			cascade.load(DetectorXMLPath+HAARCASCADE_EYE_TREE_EYEGLASSES);
			if(!cascade.empty()) {
				DetectObject.detectLargestObject(faceImg, cascade, rightEyeRect, scaledWidth);
			}
		}
		if(rightEyeRect.width <= 0) {
			cascade.load(DetectorXMLPath+HAARCASCADE_EYE);
			if(!cascade.empty()) {
				DetectObject.detectLargestObject(faceImg, cascade, rightEyeRect, scaledWidth);
			}
		}
	}
	
	                               
	/**
	 * 在人脸图像上检测眼睛
	 * @param face
	 * @param eyeCascade1
	 * @param eyeCascade2
	 * @param leftEyePoint
	 * @param rightEyePoint
	 * @param searchedLeftEye 左眼的检测区域
	 * @param searchedRightEye 右眼的检测区域
	 */
	public static void detectBothEyes(Mat face, Point leftEyePoint, Point rightEyePoint,
			Rect searchedLeftEye, Rect searchedRightEye) {
		
		final float EYE_SX = 0.16f;
		final float EYE_SY = 0.26f;
		final float EYE_SW = 0.3f;
		final float EYE_SH = 0.28f;
//		final float EYE_SX = 0.12f;
//		final float EYE_SY = 0.12f;
//		final float EYE_SW = 0.46f;
//		final float EYE_SH = 0.39f;
		int leftX = Math.round(face.cols() * EYE_SX);
		int topY = Math.round(face.rows() * EYE_SY);
		int widthX = Math.round(face.cols() * EYE_SW);
		int heightY = Math.round(face.rows() * EYE_SH);
		// 右眼角开始的x坐标
		int rightX = (int) Math.round(face.cols() * (1.0 - EYE_SX - EYE_SW));
		
		Rect tmpLeftRect = new Rect(leftX, topY, widthX, heightY);// 左半边脸的检测区域
		Mat topLeftOfFace = new Mat(face, tmpLeftRect); // 获取左半边脸
		//ImageGui.imshow(topLeftOfFace, "topLeftOfFace");
//		Imgcodecs.imwrite("res/leftface.jpg", topLeftOfFace);
		Rect tmpRightRect = new Rect(rightX, topY, widthX, heightY); // 右半边脸的检测区域
		Mat topRightOfFace = new Mat(face, tmpRightRect); // 获取右半边脸
		//Imgcodecs.imwrite("d:\\image\\rightface.jpg", topRightOfFace);
		//ImageGui.imshow(topRightOfFace, "topRightOfFace");
//		Imgcodecs.imwrite("res/rightface.jpg", topRightOfFace);
		Rect leftEyeRect = new Rect();
		Rect rightEyeRect = new Rect();
		
		// 将搜索的左眼及右眼的区域Rect 返回
		if(searchedLeftEye.width <= 0)
			ImgprocessUtils.rectReplace(searchedLeftEye, tmpLeftRect);
		if(searchedRightEye.width <= 0)
			ImgprocessUtils.rectReplace(searchedRightEye, tmpRightRect);
		
		// 检测左脸
		detectLeftEye(topLeftOfFace, leftEyeRect, topLeftOfFace.cols());
		// 检测右脸
		detectLeftEye(topRightOfFace, rightEyeRect, topRightOfFace.cols());
		
		// 检测到左眼
		if(leftEyeRect.width > 0) {
			leftEyeRect.x += leftX;
			leftEyeRect.y += topY;
			leftEyePoint.x = leftEyeRect.x + leftEyeRect.width / 2;
			leftEyePoint.y = leftEyeRect.y+leftEyeRect.height /2;
		}else {
			leftEyePoint.x = -1;
			leftEyePoint.y = -1;
		}
		
		// 如果检测右脸成功
		if(rightEyeRect.width > 0) {
			rightEyeRect.x += rightX;
			rightEyeRect.y += topY;
			rightEyePoint.x = rightEyeRect.x + rightEyeRect.width /2;
			rightEyePoint.y = rightEyeRect.y + rightEyeRect.height /2;
		}else {
			rightEyePoint.x = -1;
			rightEyePoint.y = -1;
		}
	}                              
}                                  
