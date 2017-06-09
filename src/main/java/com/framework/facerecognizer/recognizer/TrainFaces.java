package com.framework.facerecognizer.recognizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.face.BasicFaceRecognizer;
import org.opencv.face.Face;
import org.opencv.face.LBPHFaceRecognizer;

import com.framework.common.util.file.CSVFileUtils;

public class TrainFaces {

	public static void main(String[] args) {
		String opencvDLL = "G:/java/JavaProjectRelease/classchecks/src/main/webapp/WEB-INF/dll/x64/opencv_java320.dll";
		System.load(opencvDLL);
		updateTrainModel();
	}
	
	public static boolean updateTrainModel() {
		List<Mat> matLists = new ArrayList<Mat>(); // 待训练的人脸图片集合
		List<Integer> lableLists = new ArrayList<Integer>(); // 人脸图片对应的标签
		String CSVFilePath = "E:\\classchecks\\Test\\2017417\\train\\at.txt";
		CSVFileUtils.CSVRead(CSVFilePath, matLists, lableLists);

		// opencv 在训练人脸模型时需要确保人脸与标签一一对应
		if(matLists.size() == lableLists.size()) {
			Mat labels = new Mat(lableLists.size(), 1, CvType.CV_32SC1, new Scalar(0));
			for(int i = 0; i < lableLists.size(); i ++) {
				labels.put(i, 0, new int[]{lableLists.get(i)});
			}
			
			BasicFaceRecognizer faceRecognizer = load("E:\\classchecks\\test.xml");
			faceRecognizer.update(matLists, labels);
		}
		
		return false;
	}
	
	/**
	 * 以CSV文件提供的人脸图片训练人脸模型并保存模型文件(以XML的形式)
	 * @param CSVFilePath csv文件存储的绝对路径
	 * @param trainModellSavePath 训练的模型文件的保存路径
	 */
	public static boolean trainAndSaveModel(String CSVFilePath, String trainModelSavePath, String facerecAlgorithm) {
		
		if(!new File(CSVFilePath).isFile()) {
			return false;
		}
		// 判断要保存的人脸模型文件是不是一个绝对路径名
		File modelFile = new File(trainModelSavePath);
		if(!modelFile.isAbsolute()) {
			System.out.println("TrainFaces->trainAndSaveModel->trainModelSavePath不是一个绝对路径名");
			return false;
		}
		// 如果trainModelSavePath的上一级文件夹不存在则创建
		if(!modelFile.getParentFile().exists()) {
			modelFile.getParentFile().mkdirs();
		}
		
		List<Mat> matLists = new ArrayList<Mat>(); // 待训练的人脸图片集合
		List<Integer> lableLists = new ArrayList<Integer>(); // 人脸图片对应的标签
		CSVFileUtils.CSVRead(CSVFilePath, matLists, lableLists);
		
		// opencv 在训练人脸模型时需要确保人脸与标签一一对应
		if(matLists.size() == lableLists.size()) {
			Mat labels = new Mat(lableLists.size(), 1, CvType.CV_32SC1, new Scalar(0));
			for(int i = 0; i < lableLists.size(); i ++) {
				labels.put(i, 0, new int[]{lableLists.get(i)});
			}
			BasicFaceRecognizer faceRecognizer = null;
			if("FaceRecognizer.Eigenfaces".equals(facerecAlgorithm)) {
				faceRecognizer = Face.createEigenFaceRecognizer();
			} else if("FaceRecognizer.Fisherfaces".equals(facerecAlgorithm)) {
				faceRecognizer = Face.createFisherFaceRecognizer();
			}
			
			faceRecognizer.train(matLists, labels);
			faceRecognizer.save(trainModelSavePath);
			return true;
		}
		
		return false;
	}
	
	public static boolean trainLBPHAndSaveModel(String CSVFilePath, String trainModelSavePath) {
		
		if(!new File(CSVFilePath).isFile()) {
			return false;
		}
		// 判断要保存的人脸模型文件是不是一个绝对路径名
		File modelFile = new File(trainModelSavePath);
		if(!modelFile.isAbsolute()) {
			System.out.println("TrainFaces->trainAndSaveModel->trainModelSavePath不是一个绝对路径名");
			return false;
		}
		// 如果trainModelSavePath的上一级文件夹不存在则创建
		if(!modelFile.getParentFile().exists()) {
			modelFile.getParentFile().mkdirs();
		}
		
		List<Mat> matLists = new ArrayList<Mat>(); // 待训练的人脸图片集合
		List<Integer> lableLists = new ArrayList<Integer>(); // 人脸图片对应的标签
		CSVFileUtils.CSVRead(CSVFilePath, matLists, lableLists);
		
		// opencv 在训练人脸模型时需要确保人脸与标签一一对应
		if(matLists.size() == lableLists.size()) {
			Mat labels = new Mat(lableLists.size(), 1, CvType.CV_32SC1, new Scalar(0));
			for(int i = 0; i < lableLists.size(); i ++) {
				labels.put(i, 0, new int[]{lableLists.get(i)});
			}
			LBPHFaceRecognizer faceRecognizer = Face.createLBPHFaceRecognizer();
			
			faceRecognizer.train(matLists, labels);
			faceRecognizer.save(trainModelSavePath);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 加载训练的人脸模型并返回BasicFaceRecognizer模型对象
	 * @param modelFilePath
	 * @return
	 */
	public static BasicFaceRecognizer load(String modelFilePath) {
		BasicFaceRecognizer model = Face.createEigenFaceRecognizer();
		model.load(modelFilePath);
		return model;
	}
	
	public static LBPHFaceRecognizer loadLBPHModel(String modelFilePath) {
		LBPHFaceRecognizer model = Face.createLBPHFaceRecognizer();
		model.load(modelFilePath);
		return model;
	}
	
}
