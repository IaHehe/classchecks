package com.classchecks.client.student.api.clockin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.face.BasicFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.client.student.api.clockin.mapper.ClockinAsStudentMapper;
import com.classchecks.client.student.api.clockin.service.ClockinAsStudentService;
import com.classchecks.client.student.api.clockin.vo.PointVo;
import com.classchecks.client.student.api.clockin.vo.StudentClockinVo;
import com.classchecks.common.basic.service.BasicService;
import com.classchecks.common.basic.vo.SchoolCourseClockModel;
import com.framework.basic.vo.BasicEntityVo;
import com.framework.common.util.date.DateUtil;
import com.framework.common.util.file.CSVFileUtils;
import com.framework.common.util.file.FileUtils;
import com.framework.common.util.image.SaveImageUtils;
import com.framework.common.util.position.PositionUtil;
import com.framework.content.code.StudentClockInBusinessCode;
import com.framework.content.resources.ImgStoragePath;
import com.framework.facerecognizer.recognizer.PreProcessFace;
import com.framework.facerecognizer.recognizer.Recognition;

@Service
public class ClockinAsStudentServiceImpl implements ClockinAsStudentService {

	private static Logger LOG = LoggerFactory.getLogger(ClockinAsStudentServiceImpl.class);
	
	@Autowired
	private ClockinAsStudentMapper clockinAsStudentMapper;
	@Autowired
	private BasicService basicService;
	
	private boolean fileSave(CommonsMultipartFile img, String savePath, String longTime) {
		return SaveImageUtils.saveImage(img, savePath, longTime+".jpg");
	}

	@Override
	public BasicEntityVo<?> clockin(String jwAccount, String loginAccount, Double lng, Double lat,
			CommonsMultipartFile file) {
		
		LOG.info("学生端考勤Service");
		LOG.info("ContentType:" + file.getContentType() 
			+ " Filename:" + file.getOriginalFilename() + " Size:" + file.getSize());
		LOG.info("学生上传经纬度：lng=" + lng + " lat：" + lat);
		Integer teacherUId = clockinAsStudentMapper.getTeacherIDByStudentClock(jwAccount, 500);
		
		LOG.info("教师UID：" + teacherUId);
		SchoolCourseClockModel sccm = basicService.getSchoolCourseClockModelNow();
		PointVo gpsPoint = clockinAsStudentMapper.getGPSByTeacherID(teacherUId, sccm);
		LOG.info("教师最新考勤记录的GPS坐标：" + gpsPoint);
		
		double stuDistanceTea = PositionUtil.distance(gpsPoint.getLng(), gpsPoint.getLat(), lng, lat);
		
		LOG.info("学生与教师的距离：" + stuDistanceTea);
		
		if(stuDistanceTea > 550) {
			return new BasicEntityVo<>(StudentClockInBusinessCode.GPS_DISTANCE_GT_50[0], StudentClockInBusinessCode.GPS_DISTANCE_GT_50[1]);
		}
		
		Date date = new Date();
		// "yyyy-MM-dd"字符串
		String dtSimpleDate = DateUtil.dtSimpleFormat(date);
		// "yyyyMMddHHmmss"日期字符串
		String longTime = DateUtil.longDate(date);
		// 保存文件路径的每个文件夹名称数组，教师上传的图片以每天的日期作为文件夹
		String [] pathKey = {ImgStoragePath.STUDENT_CLOCK_IN_IMG_PATH, loginAccount, dtSimpleDate};
		// 保存图片的文件夹路径
		String dirSavePath = FileUtils.buildFilePath(pathKey);
		
		boolean isSaved = fileSave(file, dirSavePath, longTime);
		if(isSaved == false) { // 上传的图片保存失败
			return new BasicEntityVo<>(StudentClockInBusinessCode.BUSSINESS_IMAGE_SAVE_FAILED[0], StudentClockInBusinessCode.BUSSINESS_IMAGE_SAVE_FAILED[1]);
		}
		
		String absolutePath = FileUtils.buildFilePath(dirSavePath, longTime+".jpg");
		Mat imgSrc = Imgcodecs.imread(absolutePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE); // 加载上传的图片
		
		Mat procMat = PreProcessFace.smallProcessedFace(imgSrc);
		
		if(null == procMat) {
			return new BasicEntityVo<>(StudentClockInBusinessCode.BUSSINESS_NO_DETECT_FACE[0], StudentClockInBusinessCode.BUSSINESS_NO_DETECT_FACE[1]);
		}
		
		String collecteFaceRoute = FileUtils.buildFilePath(ImgStoragePath.PROC_FACE_IMG_SAVE_PATH, loginAccount);
		
		List<Mat> collectedMats = new ArrayList<Mat>();
		List<Integer> faceLabels = new ArrayList<Integer>();
		CSVFileUtils.loadImage(collecteFaceRoute, collectedMats, faceLabels);
		
		// 将人脸标签放入一个Mat对象，OpenCV提供的人脸识别算法中接收一个存有人脸标签的Mat
		Mat labelsMat = new Mat(faceLabels.size(), 1, CvType.CV_32SC1, new Scalar(0));
		for(int i = 0; i < faceLabels.size(); i ++) {
			labelsMat.put(i, 0, new int[]{faceLabels.get(i)});
		}
		
		// 训练人脸模型，这里使用的是特征脸算法
		BasicFaceRecognizer faceModel = Recognition.learnCollectedFaces(collectedMats, labelsMat);
		
		Mat reconstructedFace = Recognition.reconstructFace(faceModel, procMat);
		double similarity = Recognition.getSimilarity(reconstructedFace, procMat);
		LOG.info("similarity = " + similarity);
		LOG.info("predict_label: "+faceModel.predict_label(procMat));
		
		if(similarity > 0.13) {
			return new BasicEntityVo<>(StudentClockInBusinessCode.FACE_NON_EXISTENT[0], 
					StudentClockInBusinessCode.FACE_NON_EXISTENT[1]);
		}
		
		// 学生自己考勤成功后更新考勤记录
		clockinAsStudentMapper.updateStudentClockinRecord(jwAccount);
		
		StudentClockinVo vo = new StudentClockinVo();
		
		vo.setStuName(clockinAsStudentMapper.findStudentName(jwAccount));
		vo.setCurTime(DateUtil.hmsFormat(new Date()));
		// TODO 更新考勤记录
		return new BasicEntityVo<>(StudentClockInBusinessCode.BUSINESS_SUCCESS[0], 
				StudentClockInBusinessCode.BUSINESS_SUCCESS[1], vo);
	}

}
