package com.framework.facerecognizer.recognizer;


import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.face.BasicFaceRecognizer;
import org.opencv.face.Face;

public class Recognition {
	private final static Integer CV_L2 = 4;
	
	private final static double INVALID_IDENTIFICATION = 100000000.0;
	
	/**
	 * 
	* @Title: reconstructFace 
	* @Description: 从输入的预处理图像在人脸模型中重构人脸
	* @param model 包含预处理的人脸模型
	* @param preprocessedFace 输入的预处理过的图像
	* @return
	* Mat 
	* @throws
	 */
	public static Mat reconstructFace(BasicFaceRecognizer model, Mat preprocessedFace){
		try {
			// 获取每个人脸的特征值
			Mat eigenvectors = model.getEigenVectors();
			// 获取平均人脸
			Mat averageFaceRow = model.getMean();
			int faceHeight = preprocessedFace.rows();
			// subspaceProject将人脸图像投影到特征空间
			Mat projection = subspaceProject(eigenvectors, averageFaceRow, preprocessedFace.reshape(1, 1));
			// subspaceReconstruct从特征空间重构图像
			Mat reconstructionRow = subspaceReconstruct(eigenvectors, averageFaceRow, projection);
			
			Mat reconstructionMat = reconstructionRow.reshape(1, faceHeight);
			Mat reconstructedFace = new Mat(reconstructionMat.size(), CvType.CV_8U);
			reconstructionMat.convertTo(reconstructedFace, CvType.CV_8U, 1, 0);
			
			return reconstructedFace;
		} catch(CvException e) {
			e.printStackTrace();
		}
		return null;
	}
	 
	public static BasicFaceRecognizer learnCollectedFaces(List<Mat> preprocessedFaces, Mat faceLabels) {
		BasicFaceRecognizer model = Face.createEigenFaceRecognizer();
		model.train(preprocessedFaces, faceLabels);
		return model;
	}
	
	/**
	 * 计算两幅图像像素之间的相似性
	 * <p>使用基于L2范数的相对错误评价标准，该标准是将两个图像的相应像素值相减，并对所
	 * 得的差值求平方和，然后在对结果求平方根</p>
	 * @param A
	 * @param B
	 * @return
	 */
	public static double getSimilarity(Mat A, Mat B) {
		if(A.rows() > 0 && A.rows() == B.rows() && A.cols() > 0 && A.cols() == B.cols()) {
			double errorL2 = Core.norm(A, B, CV_L2);
			double similarity = errorL2 / (double)(A.rows() * A.cols());
			return similarity;
		}
		return INVALID_IDENTIFICATION;
	}

	public static Mat subspaceProject(Mat W, Mat mean, Mat src) {
		int n = src.rows();
		int d = src.cols();
		Mat X = new Mat();
		Mat Y = new Mat();
		src.convertTo(X, W.type());
		if(!mean.empty()) {
			for(int i = 0; i < n; i ++) {
				Mat r_i = X.row(i);
				Core.subtract(r_i, mean.reshape(1, 1), r_i);
			}
		}
		Core.gemm(X, W, 1.0, new Mat(), 0.0, Y);
		return Y;
	}
	
	public static Mat subspaceReconstruct(Mat W, Mat mean, Mat src) {
		int n = src.rows();
		int d = src.cols();
		Mat X = new Mat();
		Mat Y = new Mat();
		src.convertTo(Y, W.type());
		Core.gemm(Y, W, 1.0, new Mat(), 0.0, X, 2);
		if(!mean.empty()) {
			for(int i = 0; i < n; i ++) {
				Mat r_i = X.row(i);
				Core.add(r_i, mean.reshape(1, 1), r_i);
			}
		}
		return X;
	}
}
