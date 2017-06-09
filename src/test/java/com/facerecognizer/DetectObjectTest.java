package com.facerecognizer;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.framework.facerecognizer.recognizer.DetectObject;

public class DetectObjectTest {

	private static String imageDir = "E:\\classchecks\\2017417\\";
	private static String XMLFilePath = "G:\\java\\JavaProjectRelease\\classchecks\\src\\main\\resources\\haarcascades\\";
	@Test
	public void testDetectManyObject() {
		String opencvDLL = "G:/java/JavaProjectRelease/classchecks/src/main/webapp/WEB-INF/dll/x64/opencv_java320.dll";
		System.load(opencvDLL);
		
		String haarcascade = "haarcascade_frontalface_alt.xml";
		
		
		CascadeClassifier cascade = new CascadeClassifier(XMLFilePath + haarcascade);
		
		Mat src = Imgcodecs.imread(imageDir + "/split/14.jpg");
		
		MatOfRect objects = new MatOfRect();
		int scaledWidth = src.width();
		
		DetectObject.detectManyObject(src, cascade, objects, scaledWidth);
		
		Rect [] rects = objects.toArray();
		int i = 0;
		for(Rect r : rects) {
			/*Imgproc.rectangle(src, new Point(r.x-100 , r.y-100 ), 
					new Point(r.x + r.width + 80, 
							r.y + r.height + 80), new Scalar(0, 0, 255), 3);*/
			Imgproc.rectangle(src, r.tl(), 
					r.br(), new Scalar(0, 0, 255), 3);
			/*r.width += 120;
			r.height += 120;
			r.x -= 100;
			r.y -= 100;
			System.out.println(r);
			Mat roi = new Mat(src, r);
			Imgcodecs.imwrite("e:/classchecks/2017417/split/"+i+".jpg", roi);
			i ++;*/
		}
		Imgcodecs.imwrite("e:/classchecks/2017417/dectctManyObject.jpg", src);
		//Imgcodecs.imwrite("e:/classchecks/dectctManyObject.jpg", src);
	}

}
