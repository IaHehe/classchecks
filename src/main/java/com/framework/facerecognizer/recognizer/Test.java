package com.framework.facerecognizer.recognizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.face.BasicFaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.framework.common.util.file.CSVFileUtils;
import com.framework.common.util.file.FileUtils;
import com.framework.facerecognizer.image.ImageGui;

public class Test {

	public static void main(String[] args) {
		String opencvDLL = "G:/java/JavaProjectRelease/classchecks/src/main/webapp/WEB-INF/dll/x64/opencv_java320.dll";
		System.load(opencvDLL);

		
		
	}
	
	public static void recognition() {
		String modelFilePath = "E:\\classchecks\\2017417\\train\\trainModel-20170418.xml";
		BasicFaceRecognizer model = TrainFaces.load(modelFilePath);
		Mat waitRecoMat = Imgcodecs.imread("E:\\classchecks\\2017417\\split\\14.jpg");
		
		Mat preProc = PreProcessFace.rawProcessedFace(waitRecoMat);
		//Imgproc.resize(preProc, preProc, new Size(92, 112));
		Mat reconstructMat = Recognition.reconstructFace(model, preProc);
		
		double similarity = Recognition.getSimilarity(preProc, reconstructMat);
        System.out.println("similarity=" + similarity);
        int pridictLabel = model.predict_label(preProc);
        System.out.println("pridictLabel=" + pridictLabel);
	}
	
	public static void recognitionByLBPH() {
		String modelFilePath = "E:\\classchecks\\2017417\\train\\trainLBPHModel-201704171530.xml";
		LBPHFaceRecognizer model = TrainFaces.loadLBPHModel(modelFilePath);
		Mat waitRecoMat = Imgcodecs.imread("E:\\classchecks\\2017417\\split\\14.jpg");
		//Imgcodecs.imr
		Mat preProc = PreProcessFace.rawProcessedFace(waitRecoMat);
		ImageGui.imshow(preProc, "preProc");
		Imgproc.resize(preProc, preProc, new Size(92, 112));
		//Mat reconstructMat = Recognition.reconstructFace(model, preProc);
		
		//double similarity = Recognition.getSimilarity(preProc, reconstructMat);
        //System.out.println("similarity=" + similarity);
        int pridictLabel = model.predict_label(preProc);
        System.out.println("pridictLabel=" + pridictLabel);
	}
	
	public static void trainModel() {
		String trainedDir = "E:\\classchecks\\2017417\\train";
		String CSVFileSavePath = trainedDir + File.separator + "at.txt";
		String trainModelAbsolutePath = trainedDir +"\\trainModel-20170418.xml";
		//boolean isTrainSuccess = TrainFaces.trainLBPHAndSaveModel(CSVFileSavePath, trainModelAbsolutePath);
		String facerecAlgorithm = "FaceRecognizer.Eigenfaces";
		boolean isTrainSuccess = TrainFaces.trainAndSaveModel(CSVFileSavePath, trainModelAbsolutePath, facerecAlgorithm);
		
		
		System.out.println("isTrainSuccess:"+isTrainSuccess);
	}
	
	/**
	 * 以人脸图片存储的路径生成CSV标签文件
	 * @param facesPath
	 */
	public static void generateCSV(String preProcFacesPath, String CSVFileSavePath) {
		
		List<String> csvList = new ArrayList<String>();
		FileUtils.readFilePathAndMakeCSV(preProcFacesPath, csvList);
		String content = "";
		for(String name : csvList) {
			content += name + "\r\n";
		}
		CSVFileUtils.write(CSVFileSavePath, content);
	}
	// PreProcessFace.processRawImage已经改变，使用时请注意
	public static void preProcRawImg() {
		String imagesDir = "E:\\classchecks\\srcimage";//E:\\classchecks\\2017417\\single 
		String outImgDir = "E:\\classchecks\\faces";
		boolean b = PreProcessFace.processRawImage(imagesDir, outImgDir, 2);
		System.out.println(b);
	}
}
