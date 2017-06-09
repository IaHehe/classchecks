package com.framework.facerecognizer.recognizer;

import java.util.List;

import com.framework.common.util.file.FileUtils;
import com.framework.context.ServletContextHolder;
import com.framework.facerecognizer.image.*;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetectObject {
	private static Logger LOG = LoggerFactory.getLogger(DetectObject.class);
	
	/**
	 * 人脸检测
	 * 
	 * @param img
	 * @param cascade
	 * @param mor
	 * @param scaledWidth 用于收缩图像，使其检测更快
	 * @param flags
	 * @param minFeatureSize
	 * @param searchScaleFactor
	 * @param minNeighbors
	 */
	public static void detectObjectsCustom(Mat img, CascadeClassifier cascade, MatOfRect objects, 
			int scaledWidth, int flags,
			Size minFeatureSize, float searchScaleFactor, int minNeighbors) {
		Mat gray = ImgprocessUtils.covertImage2Gray(img);
		
		// 收缩图像，使其运行更快
		float scale = img.cols() / (float) scaledWidth;
		Mat resizeGray = ImgprocessUtils.resize(gray, scale, scaledWidth);
		// 高斯模糊
		Imgproc.GaussianBlur(resizeGray, resizeGray, new Size(3,3), 0, 0);
		ImageGui.imshow(resizeGray, "人脸检测-GaussianBlur");
		
		Mat equalizeHist = ImgprocessUtils.grayEqualizeHist(resizeGray);
		ImageGui.imshow(equalizeHist, "人脸检测-直方图均衡");
		
		Mat gamma = ImgprocessUtils.gammaAdjust(resizeGray);
		ImageGui.imshow(gamma, "人脸检测-Gamma");
		
		// 在灰度图中检测人脸
		////cascade.detectMultiScale(equalizedImg, objects);
		cascade.detectMultiScale(resizeGray, objects, searchScaleFactor, minNeighbors,
				flags, minFeatureSize,new Size());
		
		// 如果是用一个缩小图像进行人脸检测，得到的结果也是缩小的，因此如果想得到人脸在原始图像中的区域，就需要放大此结果
		List<Rect> rects = objects.toList();
		
		// 计算收缩比例,这里是将搜索的人脸区域放大到原图的比例
		if (img.cols() > scaledWidth) {
			for (int i = 0; i < rects.size(); i++) {
				Rect rect = rects.get(i);
				rect.x = Math.round(rect.x * scale);
				rect.y = Math.round(rect.y * scale);
				rect.width = Math.round(rect.width * scale);
				rect.height = Math.round(rect.height * scale);
			}
		}

		// 确定人脸在图像中的整个边界，防止人脸在边框之外
		for (int i = 0; i < rects.size(); i++) {
			Rect rect = rects.get(i);
			if (rect.x < 0) {
				rect.x = 0;
			}
			if (rect.y < 0) {
				rect.y = 0;
			}
			if (rect.x + rect.width > img.cols()) {
				rect.x = img.cols() - rect.width;
			}
			if (rect.y + rect.height > img.rows()) {
				rect.y = img.rows() - rect.height;
			}
		}

		// 返回存储检测到的人脸矩形的对象
		objects.fromList(rects);
	}
	
	/**
	 * 检测一张人脸
	 * @param img
	 * @param cascade
	 * @param largestObject
	 * @param scaledWidth
	 */
	public static void detectLargestObject(Mat img, CascadeClassifier cascade, 
			Rect largestObject, int scaledWidth) {
		int flags = Objdetect.CASCADE_FIND_BIGGEST_OBJECT;
		Size minFeatureSize = new Size(10, 10);
		float searchScaleFactor = 1.1f; // 表示有多少不同大小的人脸要搜索，设置的大检测更快，但正确率降低
		int minNeighbors = 4; // 使检测的正确率更高，通常设为3

		MatOfRect objects = new MatOfRect();
		detectObjectsCustom(img, cascade, objects, scaledWidth, flags, minFeatureSize, searchScaleFactor, minNeighbors);
		if (!objects.empty()) {
			Rect tmpRect = objects.toList().get(0);
			largestObject.x = tmpRect.x;
			largestObject.y = tmpRect.y;
			largestObject.width = tmpRect.width;
			largestObject.height = tmpRect.height;
		} else {// 返回一个无效的rect
			largestObject.x = -1;
			largestObject.y = -1;
			largestObject.width = -1;
			largestObject.height = -1;
		}
	}

	/**
	 * 检测多张人脸
	 * 
	 * @param img
	 * @param cascade
	 * @param objects
	 * @param scaledWidth
	 */
	public static void detectManyObject(Mat img, CascadeClassifier cascade, MatOfRect objects, 
			int scaledWidth) {
		int flags = Objdetect.CASCADE_SCALE_IMAGE;
		Size minFeatureSize = new Size(10, 10);
		float searchScaleFactor = 1.1f;
		int minNeighbors = 3;
		detectObjectsCustom(img, cascade, objects, scaledWidth, flags,
				minFeatureSize, searchScaleFactor, minNeighbors);
	}
	
	/**
	 * 
	* @Title: detectMany 
	* @Description: 检测多人图片，返回检测的人脸区域对象
	* @param mImgSRC
	* @return
	* MatOfRect
	 */
	public static MatOfRect detectMany(Mat mImgSRC) {
		
		if(mImgSRC.empty()) {
			LOG.info("检测多人图片检测时没有找到图片");
			return null;
		}
		// 人脸检测器文件的所在路径的文件夹名称数组
		String [] pathKey = {ServletContextHolder.getOpenCVHaarcascades(), "haarcascade_frontalface_alt.xml"};
		CascadeClassifier cascade = new CascadeClassifier(FileUtils.buildFilePath(pathKey));
		if(cascade.empty()) {
			LOG.info("人脸检测级联加载器为null");
			return null;
		}
		// 记录搜索到的人脸区域
		MatOfRect mOfRect = new MatOfRect();
		// 用于计算缩放比例
		int scaledWidth = mImgSRC.width();
		detectManyObject(mImgSRC, cascade, mOfRect, scaledWidth);
		if(mOfRect.toArray().length <= 0) {
			LOG.info("没有检测到人脸...");
			return null;
		}
		return mOfRect;
	}
	
	public static void main(String[] args) {
		String opencvDLL = "G:/java/OpenCV/FaceRecognizer/WebContent/WEB-INF/lib/opencv_java320.dll";
		System.load(opencvDLL);
//		String path = "E:\\classchecks\\image\\teacher\\13301236543\\2017-05-30\\20170530180803";
//		
//		for(int i = 0; i <= 10; i ++) {
//			Mat src = Imgcodecs.imread(path+"\\"+i+".jpg");
//			Mat result = PreProcessFace.smallProcessedFace(src);
//			if(result == null) {
//				System.out.println(i);
//			} else {
//				ImageGui.imshow(result, "result");
//			}
//			
//		}
		
		Mat src = Imgcodecs.imread("E:\\classchecks\\image\\teacher\\17378347850\\2017-05-31\\1.jpg");// e:/classchecks/paper/zou1.jpg
		if(src.empty()) {
			System.out.println("not found image!");
			System.exit(0);
		}
		String haarcascade = "haarcascade_frontalface_alt.xml";
//		String lbpcascade = "lbpcascade_frontalface.xml";
//		String xmlcascade = "haarcascade_eye_tree_eyeglasses.xml";
//		// 可以检测到左眼的级联加载器：haarcascade_mcs_lefteye.xml、haarcascade_righteye_2splits.xml
//		// 右眼：haarcascade_mcs_righteye.xml、haarcascade_lefteye_2splits.xml
		String XMLFilePath = Thread.currentThread().getClass().getResource("/").getPath().substring(1)
				+"haarcascades/";
		CascadeClassifier cascade = new CascadeClassifier(XMLFilePath+haarcascade);
//		CascadeClassifier cascade = 
//				new CascadeClassifier(
//						"D:/Program Files/Apache Software Foundation/apache-tomcat-8.0.39/webapps/classchecks/WEB-INF/classes/haarcascades/haarcascade_frontalface_alt.xml");
		
////==================================【双边滤波】================================
//		Mat filtered = new Mat(src.size(), CvType.CV_8U);
//		Imgproc.bilateralFilter(src, filtered, 0, 20.0, 2.0);
//		Imgcodecs.imwrite("e:/classchecks/paper/zou_dong_filtered.jpg", filtered);
//		ImageGui.imshow(filtered, "filtered");
		
////============================检测单人======================================
//		int scaledWidth = 320;
//		float scale = src.cols() / (float) scaledWidth;
//		Mat resizeMat = ImgprocessUtils.resize(src, scale,scaledWidth);
//		
//		Mat grayMat = ImgprocessUtils.covertImage2Gray(resizeMat);
//		Imgcodecs.imwrite("e:/classchecks/paper/zoudongjun-gray.jpg", grayMat);
//		 ////人脸检测
//		Rect largestObject = new Rect();
//		detectLargestObject(resizeMat, cascade, largestObject, 320);
//		
//		Imgproc.rectangle(resizeMat, new Point(largestObject.x, largestObject.y), 
//				new Point(largestObject.x + largestObject.width, 
//						largestObject.y + largestObject.height), new Scalar(0, 0, 255));
//		ImageGui.imshow(resizeMat, "检测");
		// 调用预处理
//		Mat preMat = new Mat();
//		if(largestObject.x != -1 && largestObject.y != -1) {
//			preMat = PreProcessFace.getProcessedFace(src);
//		}
//		ImageGui.imshow(preMat, "预处理结果");
		
////=============================检测多人=====================================
		MatOfRect rects = new MatOfRect();
		int scaledWidth = src.width();
		float scale = src.cols() / (float) scaledWidth;
		Mat resizeMat = ImgprocessUtils.resize(src, scale, scaledWidth);
		Mat backup = new Mat();
		resizeMat.copyTo(backup);
		Rect rect = new Rect();
		rect.x = 1097;
		rect.y = 988;
		rect.width = 325;
		rect.height=325;
		Mat x = new Mat(src, rect);
		Imgcodecs.imwrite("E:/rrr.jpg", x);
//		Mat gary = ImgprocessUtils.covertImage2Gray(resizeMat);
//		
//		detectManyObject(resizeMat, cascade, rects, scaledWidth);
//		
//		System.out.println("检测到的人脸数："+rects.toList().size());
//		for (Rect rect : rects.toArray()) {
//	    	System.out.println("rect->x:"+rect.x+" | rect->y:"+rect.y
//	    			+" | rect->width:"+rect.width+" | rect->height:"+rect.height);
//	    	Imgproc.rectangle(resizeMat, new Point(rect.x, rect.y), 
//	    			new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 2);
//	    	
//	    }
//		
////		ImageGui.imshow(resizeMat, "多人检测图");
////		Imgcodecs.imwrite("E:/detect.jpg", resizeMat);
////		ImageGui.imshow(src, "源图像");
//		List<Mat> cutImgs = ImgprocessUtils.cutOutImage(src, rects);
//		
//		if(!cutImgs.isEmpty()) {
//			int index = 1;
//			for(Mat m : cutImgs) {
//				Imgcodecs.imwrite("E:\\classchecks\\paper\\manysplit\\"+(index++)+".jpg", m);
//			}
//		}
		
//		// 裁剪图片
//		int i = 1;
//		for(Rect rect : rects.toArray()) {
//			int expandPX = 50;
//			int widthRate = (rect.x + expandPX) / rect.x;
//			int heightRate = (rect.y + expandPX) / rect.y;
//			System.out.println("widthRate="+Math.abs(Math.round(widthRate*rect.width))
//				+" , heightRate="+Math.abs(Math.round(heightRate * rect.height)));
//			rect.x -= 50;
//			rect.y -= 50;
//			rect.width += Math.abs(Math.round(widthRate*rect.width));
//			rect.height += Math.abs(Math.round(heightRate * rect.height));
//			
//			Mat split = new Mat(src, rect);
//			Imgcodecs.imwrite("E:\\classchecks\\paper\\manysplit\\"+i+".jpg", split);
//			i++;
//		}
		
		
//============================中值滤波======================================
		// 
//		Mat dst = new Mat();
//		Imgproc.medianBlur(grayMat, dst, 1);
//		ImageGui.imshow(grayMat, "灰度图");
//		ImageGui.imshow(dst, "中值滤波");
////============================Gamma 校正======================================		
//		// 
//		Mat grayMat = new Mat();
//		grayMat = ImgprocessUtils.covertImage2Gray(src);
//		Mat X = new Mat();
//		grayMat.convertTo(X, CvType.CV_32FC1);
//		Mat I = new Mat();  
//	    float gammaParams = 2.2f;
//	    Core.pow(X, gammaParams, I);
//	    
//	    Mat gammaMat = ImgprocessUtils.norm_0_255(I);
//	    Imgcodecs.imwrite("e:/classchecks/paper/zou_dong_gamma_2.2.jpg", gammaMat);
//	    ImageGui.imshow(gammaMat, "Gamma校正");
////===========================直方图均衡化=======================================
//		Mat grayMat = new Mat();
//		grayMat = ImgprocessUtils.covertImage2Gray(src);
//		Mat equalizeHist = ImgprocessUtils.grayEqualizeHist(grayMat);
//		Imgcodecs.imwrite("e:/classchecks/paper/zou_dong_equalize_hist.jpg", equalizeHist);
//		ImageGui.imshow(equalizeHist, "直方图均衡化");
////============================均值平滑======================================
		 //
//		ImageGui.imshow(grayMat, "grayMat");
//		Mat blur =  new Mat();
//		Imgproc.blur(grayMat, blur, new Size(5, 5), new Point(-1, -1));
//		ImageGui.imshow(blur, "blur");
////==================================================================		
		// 用检测到的人脸区域裁剪原图，用于检测眼睛
//		Mat detected = new Mat(resizeMat, largestObject);
//		ImageGui.imshow(detected, "resizeMat1");
//		Point leftEyePoint = new Point();
//		Point rightEyePoint = new Point();
//		Rect searchedLeftEye = new Rect();
//		Rect searchedRightEye = new Rect();
//		DetectEyes.detectBothEyes(detected, leftEyePoint, rightEyePoint, searchedLeftEye, searchedRightEye);
//		System.out.println(largestObject);
//		Imgproc.rectangle(detected, new Point(searchedLeftEye.x, searchedLeftEye.y), 
//				new Point(searchedLeftEye.x + searchedLeftEye.width, 
//						searchedLeftEye.y + searchedLeftEye.height), new Scalar(0, 0, 255));
//		Imgproc.rectangle(detected, new Point(searchedRightEye.x, searchedRightEye.y), 
//				new Point(searchedRightEye.x + searchedRightEye.width, 
//						searchedRightEye.y + searchedRightEye.height), new Scalar(0, 0, 255));
//		Imgcodecs.imwrite("e:/classchecks/paper/eyesdetect.jpg", detected);
//		ImageGui.imshow(detected, "resizeMat");
		
	}
}
