package com.classchecks.client.teacher.api.clockin.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.face.BasicFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.client.global.api.jpush.service.JPushService;
import com.classchecks.client.teacher.api.clockin.mapper.ClockinAsTeacherMapper;
import com.classchecks.client.teacher.api.clockin.service.ClockinAsTeacherService;
import com.classchecks.client.teacher.api.clockin.vo.TeachingRosterVo;
import com.classchecks.common.basic.service.BasicService;
import com.classchecks.common.basic.vo.SchoolCourseClockModel;
import com.framework.basic.vo.BasicEntityListVo;
import com.framework.common.util.GsonUtil;
import com.framework.common.util.StringUtils;
import com.framework.common.util.date.DateUtil;
import com.framework.common.util.file.CSVFileUtils;
import com.framework.common.util.file.FileUtils;
import com.framework.common.util.image.SaveImageUtils;
import com.framework.content.code.TeacherClockInBusinessCode;
import com.framework.content.resources.ImgStoragePath;
import com.framework.facerecognizer.recognizer.DetectObject;
import com.framework.facerecognizer.recognizer.ImgprocessUtils;
import com.framework.facerecognizer.recognizer.PreProcessFace;
import com.framework.facerecognizer.recognizer.Recognition;

@Service
public class ClockinAsTeacherServiceImpl implements ClockinAsTeacherService{
	private final static Logger LOG = LoggerFactory.getLogger(ClockinAsTeacherServiceImpl.class);
	@Autowired
	private BasicService basicService;
	@Autowired
	private ClockinAsTeacherMapper clockinAsTeacherMapper;
	@Autowired
	private JPushService jpushService;
	
	private boolean fileSave(CommonsMultipartFile img, String savePath, String longTime) {
		return SaveImageUtils.saveImage(img, savePath, longTime+".jpg");
	}
	
	/**
	 * 
	* @Title: faceImageDetectAndCut 
	* @Description: 人脸检测和分割
	* @param dirSavePath  图片保存的路径
	* @param longTime 当前时间，用以生成图片名
	* @return
	* List<Mat> 
	 */
	private List<Mat> faceImageDetectAndCut(String dirSavePath, String longTime) { 
		//f.getAbsoluteFile()
		String absolutePath = FileUtils.buildFilePath(dirSavePath, longTime+".jpg");
		Mat imgSrc = Imgcodecs.imread(absolutePath); // 加载上传的图片
		MatOfRect mOfRect = DetectObject.detectMany(imgSrc); // 人脸检测
		// 没有检测到人脸
		if(null == mOfRect) return null;
		
		// 在检测到人脸后，在源图像上画矩形并保存到磁盘
		Imgcodecs.imwrite(FileUtils.buildFilePath(dirSavePath, longTime+"-detect.jpg"), 
				ImgprocessUtils.rectangle(imgSrc, mOfRect.toArray()));
		
		// 分割图片
		List<Mat> faceMats = ImgprocessUtils.cutOutImage(imgSrc, mOfRect);
		// 将分割的图片保存到磁盘
		ImgprocessUtils.imwrite(faceMats, FileUtils.buildFilePath(dirSavePath, longTime));
		return faceMats;
	}
	

	
	
	
	
	@Override
	public BasicEntityListVo<?> clockin(String jwAccount, String phone, Double longitude, Double latitude, CommonsMultipartFile clockinImg) {
		long startTime = System.currentTimeMillis();
		if(clockinImg.isEmpty()) { // 上传的图片为空
			return new BasicEntityListVo<>(TeacherClockInBusinessCode.BUSSINESS_IMAGE_EMPTY[0], TeacherClockInBusinessCode.BUSSINESS_IMAGE_EMPTY[1]);
		}
		
		Date date = new Date();
		// "yyyy-MM-dd"字符串
		String dtSimpleDate = DateUtil.dtSimpleFormat(date);
		// "yyyyMMddHHmmss"日期字符串
		String longTime = DateUtil.longDate(date);
		// 保存文件路径的每个文件夹名称数组，教师上传的图片以每天的日期作为文件夹
		String [] pathKey = {ImgStoragePath.TEACHER_CLOCK_IN_IMG_PATH, phone, dtSimpleDate};
		// 保存图片的文件夹路径
		String dirSavePath = FileUtils.buildFilePath(pathKey);
		
		// 是否保存成功
		boolean isSaved = fileSave(clockinImg, dirSavePath, longTime);
		
		if(false == isSaved) { // 上传的图片保存失败
			return new BasicEntityListVo<>(TeacherClockInBusinessCode.BUSSINESS_IMAGE_SAVE_FAILED[0], TeacherClockInBusinessCode.BUSSINESS_IMAGE_SAVE_FAILED[1]);
		}
		
		
		// 检测并且分割图片
		List<Mat> faceMats = faceImageDetectAndCut(dirSavePath, longTime);
		if(faceMats.size() == 0) { // 没有检测到人脸，返回消息
			return new BasicEntityListVo<>(TeacherClockInBusinessCode.BUSSINESS_NO_DETECT_FACE[0], TeacherClockInBusinessCode.BUSSINESS_NO_DETECT_FACE[1]);
		}
		
		// =============================================================
		// 预处理分割好的图片
		List<Mat> procMats = new ArrayList<Mat>();
		LOG.info("分割的人脸数======：" + faceMats.size());
		for(int i =0; i < faceMats.size(); i ++) {
			Mat m = PreProcessFace.smallProcessedFace(faceMats.get(i));
			if(m != null && !m.empty()) {
				procMats.add(m);
			}
		}
		List<Mat> tmp =  new ArrayList<Mat>();
		CSVFileUtils.loadImage(ImgStoragePath.IMAGE_FILE_TEMP, tmp);
		LOG.info("分割的人脸成功预处理数======：" + procMats.size());	
		ImgprocessUtils.imwrite(procMats, "e:/1212/");
		// =============================================================
		// 查询当前 学年、学期、周次、星期、节次
		SchoolCourseClockModel  sccm = basicService.getSchoolCourseClockModelNow();
		LOG.info(sccm.toString());
		// 查询当前上课教师应到的学生名单
		List<TeachingRosterVo> stus = clockinAsTeacherMapper.findAllAsRoster(sccm, jwAccount);
		LOG.info("获取到教师【"+ jwAccount +"】当前要上课的学生数："+ stus.size());
		LOG.info("学生名单：" + stus.toString());
		if(stus.size() == 0) {
			return new BasicEntityListVo<>(TeacherClockInBusinessCode.BUSSINESS_NO_STU_LIST[0], TeacherClockInBusinessCode.BUSSINESS_NO_STU_LIST[1]);
		}
		// =============================================================
		// 加载收集的预处理学生图片
		// 学生预处理图片为 ： 图片保存根路径 + 手机号+具体的预处理图片
		List<Mat> collectedMats = new ArrayList<Mat>();
		List<Integer> faceLabels = new ArrayList<Integer>();
		for(int i = 0, len = stus.size(); i < len; i ++) {
			String saveRoute = FileUtils.buildFilePath(ImgStoragePath.PROC_FACE_IMG_SAVE_PATH, String.valueOf(stus.get(i).getLoginAccount()));
			CSVFileUtils.loadImage(saveRoute, collectedMats, faceLabels, stus.get(i).getStuFaceLabel());
		}
		LOG.info("collectedMats.size="+collectedMats.size());
		LOG.info("faceLabels.size="+collectedMats.size());
		// 将人脸标签放入一个Mat对象，OpenCV提供的人脸识别算法中接收一个存有人脸标签的Mat
		Mat labelsMat = new Mat(faceLabels.size(), 1, CvType.CV_32SC1, new Scalar(0));
		for(int i = 0; i < faceLabels.size(); i ++) {
			labelsMat.put(i, 0, new int[]{faceLabels.get(i)});
		}
		// 训练人脸模型，这里使用的是特征脸算法
		BasicFaceRecognizer faceModel = Recognition.learnCollectedFaces(collectedMats, labelsMat);
		// 存放识别到的标签
		List<Integer> recognizeLabels = new ArrayList<Integer>();
		// 将裁剪出的人脸的预处理集合取出依次识别
		for(int i = 0, len = tmp.size(); i < len; i ++) {
			Mat proc = tmp.get(i);
			Mat reconstructedFace = Recognition.reconstructFace(faceModel, proc);
			double similarity = Recognition.getSimilarity(reconstructedFace, proc);
			if(similarity < 0.3) {
				recognizeLabels.add(faceModel.predict_label(tmp.get(i)));
			}
		}
		LOG.info("识别到的标签：" + recognizeLabels.toString());
		// 将识别结果与学生名单比对，得出正常考勤名单和没有识别到的学生名单
		List<TeachingRosterVo> notRecognized = new ArrayList<>();
		for(int i = 0, len = stus.size(); i < len; i ++) {
			if(recognizeLabels.contains(stus.get(i).getStuFaceLabel())) {
				stus.get(i).setClockinType(TeachingRosterVo.CLOCK_IN_OK);
			} else {
				stus.get(i).setClockinType(TeachingRosterVo.CLOCK_IN_ABSENCE);
				notRecognized.add(stus.get(i));
			}
		}//LOG.info("相识度->similarity："+similarity);
//		String str = "[{\"id\":1042,\"studentName\":\"王乙洁\",\"stuJWAccount\":\"2015440493\",\"loginAccount\":\"18996316514\",\"stuFaceLabel\":58,\"stuClass\":\"计科2015-03\",\"clockinType\":2},{\"id\":1053,\"studentName\":\"李灵黛\",\"stuJWAccount\":\"2013440001\",\"loginAccount\":\"13200000001\",\"stuFaceLabel\":45,\"regID\":\"100d8559097725d211b\",\"stuClass\":\"计科2014-02\",\"clockinType\":2},{\"id\":1054,\"studentName\":\"冷文卿\",\"stuJWAccount\":\"2013440002\",\"loginAccount\":\"13200000002\",\"stuFaceLabel\":46,\"stuClass\":\"计科2014-03\",\"clockinType\":2},{\"id\":1055,\"studentName\":\"顾西风\",\"stuJWAccount\":\"2013440003\",\"loginAccount\":\"13200000003\",\"stuFaceLabel\":47,\"stuClass\":\"计科2014-04\",\"clockinType\":1},{\"id\":1056,\"studentName\":\"统月\",\"stuJWAccount\":\"2013440004\",\"loginAccount\":\"13200000004\",\"stuFaceLabel\":48,\"stuClass\":\"计科2014-01\",\"clockinType\":1},{\"id\":1057,\"studentName\":\"苏普\",\"stuJWAccount\":\"2013440005\",\"loginAccount\":\"13200000005\",\"stuFaceLabel\":49,\"stuClass\":\"计科2014-02\",\"clockinType\":1},{\"id\":1058,\"studentName\":\"江城子\",\"stuJWAccount\":\"2013440006\",\"loginAccount\":\"13200000006\",\"stuFaceLabel\":50,\"stuClass\":\"计科2014-03\",\"clockinType\":2},{\"id\":1059,\"studentName\":\"柳长街\",\"stuJWAccount\":\"2013440007\",\"loginAccount\":\"13200000007\",\"stuFaceLabel\":51,\"stuClass\":\"计科2014-04\",\"clockinType\":1},{\"id\":1060,\"studentName\":\"柳辰飞\",\"stuJWAccount\":\"2013440008\",\"loginAccount\":\"13200000008\",\"stuFaceLabel\":52,\"stuClass\":\"计科2014-01\",\"clockinType\":1},{\"id\":1061,\"studentName\":\"夏舒征\",\"stuJWAccount\":\"2013440009\",\"loginAccount\":\"13200000009\",\"stuFaceLabel\":53,\"stuClass\":\"计科2014-02\",\"clockinType\":1},{\"id\":1062,\"studentName\":\"慕容冲\",\"stuJWAccount\":\"2013440010\",\"loginAccount\":\"13200000010\",\"stuFaceLabel\":54,\"stuClass\":\"计科2014-03\",\"clockinType\":1},{\"id\":1063,\"studentName\":\"萧合凰\",\"stuJWAccount\":\"2013440011\",\"loginAccount\":\"13200000011\",\"stuFaceLabel\":55,\"stuClass\":\"计科2014-04\",\"clockinType\":2},{\"id\":1064,\"studentName\":\"燕七\",\"stuJWAccount\":\"2013440012\",\"loginAccount\":\"13200000012\",\"stuFaceLabel\":56,\"stuClass\":\"计科2014-01\",\"clockinType\":1}]";
//		List<TeachingRosterVo> tt = GsonUtil.jsonToList(str, TeachingRosterVo.class);
//		LOG.info("-----1----->>"+tt.get(0).getLoginAccount());
//		String notRecoStr= "[{\"id\":1042,\"studentName\":\"王乙洁\",\"stuJWAccount\":\"2015440493\",\"loginAccount\":\"18996316514\",\"stuFaceLabel\":58,\"stuClass\":\"计科2015-03\",\"clockinType\":2},{\"id\":1053,\"studentName\":\"李灵黛\",\"stuJWAccount\":\"2013440001\",\"loginAccount\":\"13200000001\",\"stuFaceLabel\":45,\"regID\":\"100d8559097725d211b\",\"stuClass\":\"计科2014-02\",\"clockinType\":2},{\"id\":1054,\"studentName\":\"冷文卿\",\"stuJWAccount\":\"2013440002\",\"loginAccount\":\"13200000002\",\"stuFaceLabel\":46,\"stuClass\":\"计科2014-03\",\"clockinType\":2},{\"id\":1058,\"studentName\":\"江城子\",\"stuJWAccount\":\"2013440006\",\"loginAccount\":\"13200000006\",\"stuFaceLabel\":50,\"stuClass\":\"计科2014-03\",\"clockinType\":2},{\"id\":1063,\"studentName\":\"萧合凰\",\"stuJWAccount\":\"2013440011\",\"loginAccount\":\"13200000011\",\"stuFaceLabel\":55,\"stuClass\":\"计科2014-04\",\"clockinType\":2}]";
//		List<TeachingRosterVo> notTt = GsonUtil.jsonToList(notRecoStr, TeachingRosterVo.class);
//		LOG.info("-----2----->>"+notTt.get(0).getLoginAccount());
		//LOG.info();
		LOG.info("返回给教师端的考勤结果：" + stus.toString());
		LOG.info("没有识别到的学生名单：" + notRecognized.toString());
		
		boolean saveClockInInfo = saveClockInRecord(jwAccount, longitude, latitude, stus);
		LOG.info("保存考勤识别结果：saveClockInInfo = " + saveClockInInfo);
		if(saveClockInInfo == false) {
			return new BasicEntityListVo<>(TeacherClockInBusinessCode.BUSSINESS_SQL_EXCEPTION[0], TeacherClockInBusinessCode.BUSSINESS_SQL_EXCEPTION[1]);
		}
		// 对没有识别到的学生推送通知，让学生自己打卡考勤
		List<String> stuRegIds = new ArrayList<String>();
		for(int i = 0, len = notRecognized.size(); i < len; i ++) {
			if(StringUtils.notBlank(notRecognized.get(i).getRegID())) {
				stuRegIds.add(notRecognized.get(i).getRegID());
			}
		}
		jpushService.pushStuClockByRegId(stuRegIds);
		/// 
		long endTime = System.currentTimeMillis();
		LOG.info("教师考勤处理用时：" + (endTime-startTime) / 1000 + "秒");
		return new BasicEntityListVo<TeachingRosterVo>(TeacherClockInBusinessCode.BUSINESS_SUCCESS[0], TeacherClockInBusinessCode.BUSINESS_SUCCESS[1], stus);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.classchecks.client.teacher.api.clockin.service.ClockinAsTeacherService#saveClockInRecord(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@Transactional(readOnly=false, rollbackFor=RuntimeException.class)
	public boolean saveClockInRecord(String jwAccount, Double lng, Double lat, List<TeachingRosterVo> studentList) {

		try {
			// 获得现在的学校时钟
			// 获得现在指定教师的课程时间ID
			SchoolCourseClockModel schoolCourseClockModel = basicService.getSchoolCourseClockModelNow();
			Integer idCourseTime = clockinAsTeacherMapper.getCourseTimeByTeacherNoNow(jwAccount, schoolCourseClockModel);
			if (idCourseTime == null) {
				LOG.debug("获取教师课程时间ID时失败，未找到");
				return false;
			}
			// 添加教师考勤记录
			clockinAsTeacherMapper.insertClockInRecord(jwAccount, idCourseTime, lng, lat, schoolCourseClockModel);
			// 添加学生考勤记录
			clockinAsTeacherMapper.insertStudentClockInRecord(idCourseTime, schoolCourseClockModel.getWeek(), studentList);
			return true;
		} catch(RuntimeException ex) {
			LOG.info("数据库异常.....", ex);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}
// [TeachingRosterVo [id=1042, studentName=王乙洁 , stuJWAccount=2015440493, loginAccount=17388243270, stuFaceLabel=43, regID=100d8559097725d211b, stuClass=计科2015-03, clockinType=2], TeachingRosterVo [id=1042, studentName=王乙洁 , stuJWAccount=2015440493, loginAccount=18996316514, stuFaceLabel=58, regID=null, stuClass=计科2015-03, clockinType=2], TeachingRosterVo [id=1053, studentName=李灵黛, stuJWAccount=2013440001, loginAccount=13200000001, stuFaceLabel=45, regID=100d8559097725d211b, stuClass=计科2014-02, clockinType=2], TeachingRosterVo [id=1054, studentName=冷文卿, stuJWAccount=2013440002, loginAccount=13200000002, stuFaceLabel=46, regID=null, stuClass=计科2014-03, clockinType=2], TeachingRosterVo [id=1055, studentName=顾西风, stuJWAccount=2013440003, loginAccount=13200000003, stuFaceLabel=47, regID=null, stuClass=计科2014-04, clockinType=1], TeachingRosterVo [id=1056, studentName=统月, stuJWAccount=2013440004, loginAccount=13200000004, stuFaceLabel=48, regID=null, stuClass=计科2014-01, clockinType=1], TeachingRosterVo [id=1057, studentName=苏普, stuJWAccount=2013440005, loginAccount=13200000005, stuFaceLabel=49, regID=null, stuClass=计科2014-02, clockinType=1], TeachingRosterVo [id=1058, studentName=江城子, stuJWAccount=2013440006, loginAccount=13200000006, stuFaceLabel=50, regID=null, stuClass=计科2014-03, clockinType=2], TeachingRosterVo [id=1059, studentName=柳长街, stuJWAccount=2013440007, loginAccount=13200000007, stuFaceLabel=51, regID=null, stuClass=计科2014-04, clockinType=1], TeachingRosterVo [id=1060, studentName=柳辰飞, stuJWAccount=2013440008, loginAccount=13200000008, stuFaceLabel=52, regID=null, stuClass=计科2014-01, clockinType=1], TeachingRosterVo [id=1061, studentName=夏舒征, stuJWAccount=2013440009, loginAccount=13200000009, stuFaceLabel=53, regID=null, stuClass=计科2014-02, clockinType=1], TeachingRosterVo [id=1062, studentName=慕容冲, stuJWAccount=2013440010, loginAccount=13200000010, stuFaceLabel=54, regID=null, stuClass=计科2014-03, clockinType=1], TeachingRosterVo [id=1063, studentName=萧合凰, stuJWAccount=2013440011, loginAccount=13200000011, stuFaceLabel=55, regID=null, stuClass=计科2014-04, clockinType=2], TeachingRosterVo [id=1064, studentName=燕七, stuJWAccount=2013440012, loginAccount=13200000012, stuFaceLabel=56, regID=null, stuClass=计科2014-01, clockinType=1]]
}
