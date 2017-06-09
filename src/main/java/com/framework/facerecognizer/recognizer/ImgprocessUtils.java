package com.framework.facerecognizer.recognizer;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.framework.common.util.file.FileUtils;
import com.framework.facerecognizer.image.ImageGui;

public class ImgprocessUtils {
	/**
	 * 重新为oldPoint赋值
	 * @param oldPoint 需要替换的point对象
	 * @param newPoint 
	 */
	public static void pointReplace(Point oldPoint, Point newPoint) {
		oldPoint.x = newPoint.x;
		oldPoint.y = newPoint.y;
	}
	
	public static void rectReplace(Rect oldRect, Rect newRect) {
		oldRect.x = newRect.x;
		oldRect.y = newRect.y;
		oldRect.width = newRect.width;
		oldRect.height = newRect.height;
	}
	
	/**
	 * 将源图像转换为灰度图
	 * @param srcImg
	 * @return
	 */
	public static Mat covertImage2Gray(Mat srcImg) {
		Mat gray = new Mat(); // 存储灰度图
		if (srcImg.channels() == 3) {
			Imgproc.cvtColor(srcImg, gray, Imgproc.COLOR_BGR2GRAY);
		} else if (srcImg.channels() == 4) {
			Imgproc.cvtColor(srcImg, gray, Imgproc.COLOR_BGRA2GRAY);
		} else {
			gray = srcImg;
		}
		return gray;
	}
	/**
	 * 
	* @Title: resize 
	* @Description: 缩放图片
	* @param srcImg
	* @param scale
	* @param scaledWidth
	* @return
	* Mat 
	* @throws
	 */
	public static Mat resize(Mat srcImg, float scale, int scaledWidth) {
		Mat inputImg = new Mat();
		// 计算收缩比例
		//float scale = srcImg.cols() / (float) scaledWidth;
		if (srcImg.cols() > scaledWidth) {
			// 缩小图像，同时保持相同的纵横比
			// Math.round == cvRound(javacv没有cvRound)
			int scaledHeight = Math.round(srcImg.rows() / scale);
			Imgproc.resize(srcImg, inputImg, new Size(scaledWidth, scaledHeight));
		} else {
			// 当图片足够小的时候，直接使用
			srcImg.copyTo(inputImg);
		}
		return inputImg;
	}

	/**
	 * 图像归一化
	* @Title: norm_0_255 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param src
	* @return
	* Mat 
	* @throws
	 */
	public static Mat norm_0_255(Mat src) {
		// 创建和返回一个归一化后的图像矩阵
		Mat dst = new Mat();
		switch(src.channels()) {
			case 1: Core.normalize(src, dst, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1); break;
			case 3: Core.normalize(src, dst, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC3); break;
			default: src.copyTo(dst);break;
		}
		return dst;
	}
	
	/**
	 * 
	* @Title: grayEqualizeHist 
	* @Description: 直方图均衡化
	* @param grayImg
	* @return
	* Mat 
	* @throws
	 */
	public static Mat grayEqualizeHist(Mat grayImg) {
		//Mat gray = new Mat();
		//Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		Mat heqResult = new Mat(); // 直方图均衡化
		Imgproc.equalizeHist(grayImg, heqResult);
		return heqResult;
	}
	
	/**
	 * 
	* @Title: gammaAdjust 
	* @Description: gamma校正
	* @param grayImg
	* @return
	* Mat 
	* @throws
	 */
	public static Mat gammaAdjust(Mat grayImg) {
		
		Mat X = new Mat();
		grayImg.convertTo(X, CvType.CV_32FC1);
		Mat I = new Mat();
	    float gamma = 1/2.2f;
	    Core.pow(X, gamma, I);
	    
	    Mat result = norm_0_255(I);
	    return result;
	}
	
	/**
	 * 其主要思路为：
		1、求取源图I的平均灰度，并记录rows和cols；
		2、按照一定大小，分为N*M个方块，求出每块的平均值，得到子块的亮度矩阵D；
		3、用矩阵D的每个元素减去源图的平均灰度，得到子块的亮度差值矩阵E；
		4、用双立方差值法，将矩阵E差值成与源图一样大小的亮度分布矩阵R；
		5、得到矫正后的图像result=I-R；
	* @Title: unevenLightCompensate 
	* @Description: 光线补偿 
	* @param image
	* @param blockSize
	* void 
	* @throws
	 */
	public static void unevenLightCompensate(Mat image, int blockSize) {
		if(image.channels() == 3) {
			Imgproc.cvtColor(image, image, 7);
		}
		double average = Core.mean(image).val[0];
		Scalar scalar = new Scalar(average);
		int rowsNew = (int) Math.ceil((double)image.rows() / (double)blockSize);
		int colsNew = (int) Math.ceil((double)image.cols() / (double)blockSize);
		Mat blockImage = new Mat();
		blockImage = Mat.zeros(rowsNew, colsNew, CvType.CV_32FC1);
		for(int i = 0; i < rowsNew; i ++) {
			for(int j = 0; j < colsNew; j ++) {
				int rowmin = i * blockSize;
				int rowmax = (i + 1) * blockSize;
				if(rowmax > image.rows()) rowmax = image.rows();
				int colmin = j * blockSize;
				int colmax = (j +1) * blockSize;
				if(colmax > image.cols()) colmax = image.cols();
				Range rangeRow = new Range(rowmin, rowmax);
				Range rangeCol = new Range(colmin, colmax);
				Mat imageROI = new Mat(image, rangeRow, rangeCol);
				double temaver = Core.mean(imageROI).val[0];
				blockImage.put(i, j, temaver);
			}
		}
		
		Core.subtract(blockImage, scalar, blockImage);
		Mat blockImage2 = new Mat();
		int INTER_CUBIC = 2;
		Imgproc.resize(blockImage, blockImage2, image.size(), 0, 0, INTER_CUBIC);
		Mat image2 = new Mat();
		image.convertTo(image2, CvType.CV_32FC1);
		Mat dst = new Mat();
		Core.subtract(image2, blockImage2, dst);
		dst.convertTo(image, CvType.CV_8UC1);
	}

	/**
	 * 
	* @Title: cutOutImage 
	* @Description: 以Rect坐标裁剪图片 
	* @param src
	* @param mOfRect
	* @return
	* List<Mat> 
	 */
	public static List<Mat> cutOutImage(Mat src, MatOfRect mOfRect) {
		
		List<Mat> outs = new ArrayList<Mat>();
		
		if(src.empty() || mOfRect.toArray().length <= 0) {
			return null;
		}
		
		int enlarge = 50; //  将图片的x，y坐标加大
		for(Rect r : mOfRect.toArray()) {
			int enlargeW = (r.x + enlarge) / r.x;
			int enlargeH = (r.y + enlarge) / r.y;
			r.x -= enlarge;
			r.y -= enlarge;
			r.width += (enlargeW * r.width);
			r.height += (enlargeH * r.height);
			
			// 确定人脸在图像中的整个边界，防止人脸在边框之外
			if (r.x < 0) {
				r.x = 0;
			}
			if (r.y < 0) {
				r.y = 0;
			}
			if (r.x + r.width > src.cols()) {
				r.x = src.cols() - r.width;
			}
			if (r.y + r.height > src.rows()) {
				r.y = src.rows() - r.height;
			}
		
			
			Mat cutting = new Mat(src, r); // 裁剪图片
//			System.out.println(cutting);
			outs.add(cutting);
		}
		return outs;
	}
	
	/**
	 * 
	* @Title: rectangle 
	* @Description: 以检测的人脸图像区域数组在源图像上画矩形框
	* @param mImgSRC
	* @param rects
	* @return
	* Mat 
	 */
	public static Mat rectangle(Mat mImgSRC, Rect ...rects ) {
		Mat tmp = new Mat();
		mImgSRC.copyTo(tmp);
		for(Rect r : rects) {
			Imgproc.rectangle(tmp, new Point(r.x, r.y), new Point(r.x + r.width, r.y + r.height), new Scalar(0, 0, 255));
		}
		
		return tmp;
	}
	
	public static void imwrite(List<Mat> mats, String filePath) {
		FileUtils.isExist(filePath);
		for(int i = 0, len = mats.size(); i < len; i ++) {
			Imgcodecs.imwrite(FileUtils.buildFilePath(filePath, i+".jpg"), mats.get(i));
		}
	}
	
	public static void main(String[] args) {
		String opencvDLL = "G:/java/JavaProjectRelease/classchecks/src/main/webapp/WEB-INF/dll/x64/opencv_java320.dll";
		System.load(opencvDLL);
		
		Mat src = Imgcodecs.imread("e:/classchecks/paper/zoudongjun.jpg");//e:/classchecks/Test/13.jpg G:/C++/images/flower3.jpg
		int scaledWidth = 320;
		float scale = src.cols() / (float) scaledWidth;
		Mat resizeMat = resize(src, scale, scaledWidth);
		Imgcodecs.imwrite("e:/classchecks/paper/resize.jpg", resizeMat);
		/*grayEqualizeHist(src);*/
		//unevenLightCompensate(src, 32);
		ImageGui.imshow(resizeMat, "src");
	}
}

