package com.framework.facerecognizer.recognizer;


import java.io.File;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.common.util.file.ImageFileFilter;
import com.framework.context.ServletContextHolder;
import com.framework.facerecognizer.image.ImageGui;

public class PreProcessFace {
	
	private static Logger LOG = LoggerFactory.getLogger(PreProcessFace.class);
	
	private static final double CV_PI = 3.1415926535897932384626433832795;
//	private static final double DESIRED_LEFT_EYE_X = 0.16;
//	private static final double DESIRED_LEFT_EYE_Y = 0.14;
//	private static final double FACE_ELLIPSE_CY = 0.40;
//	private static final double FACE_ELLIPSE_W = 0.50;
//	private static final double FACE_ELLIPSE_H = 0.80;
	private static final double DESIRED_LEFT_EYE_X = 0.16;
	private static final double DESIRED_LEFT_EYE_Y = 0.14;
	private static final double FACE_ELLIPSE_CY = 0.40;
	private static final double FACE_ELLIPSE_W = 0.50;
	private static final double FACE_ELLIPSE_H = 0.80;
	
	public static void equalizeLeftAndRightHalves(Mat faceImg) {
		int w = faceImg.cols();
		int h = faceImg.rows();
		
		Mat wholeFace = new Mat();
		Imgproc.equalizeHist(faceImg, wholeFace);
		
		int midX = w /2;
		Mat leftSide = new Mat(faceImg, new Rect(0, 0, midX, h));
		Mat rightSide = new Mat(faceImg, new Rect(midX, 0, w-midX, h));
	
		Imgproc.equalizeHist(leftSide, leftSide);
		Imgproc.equalizeHist(rightSide, rightSide);
		
		for(int y = 0; y < h; y ++) {
			for(int x = 0; x < w; x ++) {
				double v[] = new double[]{0};
				if(x < w / 4) {
					v = leftSide.get(y, x);
				} else if(x < w*2/4) {
					double[] lv = leftSide.get(y, x);
					double [] wv = wholeFace.get(y, x);
					float f = (x - w*1/4) / (float)(w*0.25f);
					v[0] = Math.round(((1.0f - f) * lv[0] + (f) * wv[0]));
				} else if(x < w*3/4) {
					double [] rv = rightSide.get(y, x-midX);
					double [] wv = wholeFace.get(y, x);
					float f = (x - w*2/4) / (float)(w*0.25f);
					v[0] = Math.round((1.0f -f) * wv[0]+(f)*rv[0]);
				} else {
					v = rightSide.get(y, x-midX);
				}
				faceImg.put(y, x, v);
			} // end x loop
		} // end y loop
		
		ImageGui.imshow(faceImg, "对左右区域均衡化");
	}
	
	/**
	 * 预处理人脸
	 * @param srcImg
	 * @param desiredFaceWidth
	 * @param faceCascade
	 * @param eyeCascade1
	 * @param eyeCascade2
	 * @param doLeftAndRightSeparately
	 * @param storeFaceRect
	 * @param storeLeftEye
	 * @param storeRightEye
	 * @param searchedLeftEye
	 * @param searchedRightEye
	 * @return
	 */
	public static Mat pretreatmentFace(Mat srcImg, int desiredFaceWidth, CascadeClassifier faceCascade, 
			boolean doLeftAndRightSeparately, Rect storeFaceRect, int scaledWidth) {
		LOG.info("正在处理人脸");
		
		int desiredFaceHeight = desiredFaceWidth;
		if(storeFaceRect.width <= 0) {
			storeFaceRect.width = -1;
		}
		
		// 在图像中找到最大的一张人脸
		Rect faceRect = new Rect();
		DetectObject.detectLargestObject(srcImg, faceCascade, faceRect, scaledWidth);
		
		// 检查是否在图像中检测到人脸
		if(faceRect.width > 0) {
			if(storeFaceRect.x <= 0) {
				ImgprocessUtils.rectReplace(storeFaceRect, faceRect);
			}
			
			// 以获取的人脸区域faceRect，在源图像上裁剪，获取到人脸图像
			Mat faceImg = new Mat(srcImg, faceRect);
			//ImageGui.imshow(faceImg, "在原图上获取到的人脸区域");
			// 如果输入的源图像不是一个灰度图，将图像转换成BGR或者BGRA的灰度图
			Mat gray = new Mat();
			if(faceImg.channels() == 3) {
				Imgproc.cvtColor(faceImg, gray, Imgproc.COLOR_BGR2GRAY);
			}else if(faceImg.channels() == 4) {
				Imgproc.cvtColor(faceImg, gray, Imgproc.COLOR_BGRA2BGR);
			}else {
				gray = faceImg;
			}
			//ImageGui.imshow(gray, "gray");
			// 在获取的人脸灰度图上检测眼睛
			Point leftEyePoint = new Point();
			Point rightEyePoint = new Point();
			Rect searchedLeftEye = new Rect();
			Rect searchedRightEye = new Rect();
			DetectEyes.detectBothEyes(gray, leftEyePoint, rightEyePoint, searchedLeftEye, searchedRightEye);
			

			
			//如果检测到双眼
			if(leftEyePoint.x >= 0 && rightEyePoint.x >= 0) {
				
				// 计算2只眼的中心
				// 注意C++中使用Point2f
				Point eyesCenter = new Point((leftEyePoint.x + rightEyePoint.x) * 0.5f, (leftEyePoint.y + rightEyePoint.y) * 0.5f);
				// get the angle between the 2 eyes
				double dy = (rightEyePoint.y - leftEyePoint.y);
				double dx = (rightEyePoint.x - leftEyePoint.x);
				double len = Math.sqrt(dx*dx + dy*dy);
				
				double angle = Math.atan2(dy, dx) * 180.0 / CV_PI; // 弧度转换成角度.
				LOG.info("angle="+angle);
				// Hand measurements shown that the left eye center should ideally be at roughly (0.19, 0.14) of a scaled face image.
				final double DESIRED_RIGHT_EYE_X = (1.0f - DESIRED_LEFT_EYE_X);
				// Get the amount we need to scale the image to be the desired fixed size we want.
				double desiredLen = (DESIRED_RIGHT_EYE_X - DESIRED_LEFT_EYE_X) * desiredFaceWidth;
				double scale = desiredLen / len;
				LOG.info("scale="+scale);
				// 仿射变换矩阵
				//Mat rot_mat = Imgproc.getRotationMatrix2D(eyesCenter, -2.5, scale);
				Mat rot_mat = Imgproc.getRotationMatrix2D(eyesCenter, angle, scale);
				
				// 变换人脸，是眼睛出现在人脸所需位置
				double [] d = rot_mat.get(0, 2);
				for(int i = 0; i < d.length; i ++) {
					d[i] += ( desiredFaceWidth * 0.5f - eyesCenter.x);
				}
				rot_mat.put(0, 2, d);
				
				double [] d1 = rot_mat.get(1, 2);
				for(int i = 0; i < d1.length; i ++) {
					d1[i] += desiredFaceHeight * DESIRED_LEFT_EYE_Y - eyesCenter.y;
				}
				rot_mat.put(1, 2, d1);
				// 仿射变换
				/*	CV_8U and CV_8S -> byte[],
					CV_16U and CV_16S -> short[],
					CV_32S -> int[],
					CV_32F -> float[],
					CV_64F-> double[].
				*/
				Mat warped = new Mat(desiredFaceHeight, desiredFaceWidth, CvType.CV_8U, new Scalar(128));// Clear the output image to a default grey.
				Imgproc.warpAffine(gray, warped, rot_mat, warped.size());
				ImageGui.imshow(warped, "PreProcessFace->warped");
				if(!doLeftAndRightSeparately) {
					// Do it on the whole face.
					Imgproc.equalizeHist(warped, warped);
				} else {
					// Do it seperately for the left and right sides of the face.
					equalizeLeftAndRightHalves(warped);
				}
				//ImageGui.imshow(warped, "equalizeHist->warped");
				
				// 中值滤波
//				Mat median = new Mat(warped.size(), CvType.CV_8U);
//				Imgproc.medianBlur(warped, median, 5);	
				
				Mat filtered = new Mat(warped.size(), CvType.CV_8U);
				Imgproc.bilateralFilter(warped, filtered, 0, 20.0, 2.0);
				//ImageGui.imshow(filtered, "PreProcessFace->filtered");
				
				Mat mask = new Mat(warped.size(), CvType.CV_8U, new Scalar(0));// Start with an empty mask.
				Point faceCenter = new Point(desiredFaceWidth /2, Math.round(desiredFaceHeight * FACE_ELLIPSE_CY));
				Size size = new Size(Math.round(desiredFaceWidth * FACE_ELLIPSE_W), Math.round(desiredFaceHeight * FACE_ELLIPSE_H));
				Imgproc.ellipse(mask, faceCenter, size, 0, 0, 360, new Scalar(255), -1);
//				ImageGui.imshow(mask, "mask");
				
				Mat dstImg = new Mat(warped.size(), CvType.CV_8U, new Scalar(128));
				
				filtered.copyTo(dstImg, mask);
				
				return dstImg;
			} else{
				if(leftEyePoint.x <= 0) {
					System.out.println("将要返回一个空的Mat: 没有检测到--> 左 <--眼");
				}else {
					System.out.println("将要返回一个空的Mat: 没有检测到--> 右 <--眼");
				}
				return null;
			}
		} else{
			System.out.println("将要返回一个空的Mat: 没有检测到--> 人脸 <--");
			return null;
		}
	}
	
	/**
	 * 
	* @Title: rawProcessedFace 
	* @Description: 收集的原始图片预处理
	* @param faceImg
	* @return
	* Mat 
	* @throws
	 */
	public static Mat rawProcessedFace(Mat faceImg) {
		String DetectorFilename = "haarcascade_frontalface_alt.xml";
//		String XMLFilePath = Thread.currentThread().getClass().getResource("/").getPath().substring(1)
//				+"haarcascades/";
		String DetectorXMLPath = ServletContextHolder.getOpenCVHaarcascades() + DetectorFilename;
		LOG.info("DetectorXMLPath------>>>"+DetectorXMLPath);
		CascadeClassifier faceCascade = new CascadeClassifier(DetectorXMLPath);
		int faceWidth = 320;
		boolean preprocessLeftAndRightSeparately = true;
		Rect faceRect = new Rect();
		Mat getFace = PreProcessFace.pretreatmentFace(faceImg, faceWidth, faceCascade, 
				preprocessLeftAndRightSeparately, faceRect, faceWidth);
		
		return getFace;
	}
	
	public static Mat smallProcessedFace(Mat faceImg) {
		String DetectorFilename = "haarcascade_frontalface_alt.xml";
//		String XMLFilePath = Thread.currentThread().getClass().getResource("/").getPath().substring(1)
//				+"haarcascades/";
		String DetectorXMLPath = ServletContextHolder.getOpenCVHaarcascades() + DetectorFilename;
		LOG.info("DetectorXMLPath------>>>"+DetectorXMLPath);
		CascadeClassifier faceCascade = new CascadeClassifier(DetectorXMLPath);
		int faceWidth = 320;
		boolean preprocessLeftAndRightSeparately = true;
		Rect faceRect = new Rect();
		Mat getFace = PreProcessFace.pretreatmentFace(faceImg, faceWidth, faceCascade, 
				preprocessLeftAndRightSeparately, faceRect, faceWidth);
		
		return getFace;
	}
	
	/**
	 * 
	* @Title: processRawImage 
	* @Description: 处理原始图片， 对用户上传的人脸图片进行预处理
	* @param rawFacePathDir 原始图像的保存根路径(根文件有所有单人的图像，单人图像以注册的手机号为文件夹存储)
	* @param processedSavePathDir 处理后要保存的根路径
	* @return
	* boolean 
	 */
	public static boolean processRawImage(String rawFacePathDir, String processedSavePathDir, int faceLabel) {
		File rawRoot = new File(rawFacePathDir);
		
		// rawRoot不存在或者不是一个文件夹，返回false
		if(!rawRoot.exists() || !rawRoot.isDirectory()) 
			return false;
		
		//如果输出路径不存在，则创建文件夹
		File outRoot = new File(processedSavePathDir);
		if(!outRoot.exists()) {
			outRoot.mkdirs();
		}
		
		File[] rawImages = rawRoot.listFiles(new ImageFileFilter());
		// 用预处理根路径的文件夹名字作为用户预处理图片的标签
		for(int i = 0; i < rawImages.length; i ++) {
			Mat m = Imgcodecs.imread(rawImages[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			LOG.info("正在处理:" + rawImages[i]);
			Mat preproc = rawProcessedFace(m);
			if(preproc != null) {
				String filename = processedSavePathDir + File.separator + faceLabel +"_" + (i+1) +".jpg";
				Imgcodecs.imwrite(filename, preproc);
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		String opencvDLL = "G:/java/OpenCV/FaceRecognizer/WebContent/WEB-INF/lib/opencv_java320.dll";
		System.load(opencvDLL);
		
		int dirLen = 12;
		String raw_route = "E:\\jiaolaoshi";
		String proc_save_route = "E:\\jiaolaoshi\\proc";
		int faceLabel = 45;
		processRawImage(raw_route, proc_save_route, faceLabel);
		/*for(int i = 1; i <= dirLen; i ++) {
			
			String phone = "132000000";
			if(i < 10) {
				 phone += ("0"+i);
			} else {
				phone += i;
			}
			processRawImage(raw_route+File.separator+phone, proc_save_route+File.separator+phone, faceLabel);
			++ faceLabel;
		}*/
	}
	
}