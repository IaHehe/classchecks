package com.classchecks.client.student.api.register.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.client.global.api.sms.mapper.SMSCodeMapper;
import com.classchecks.client.student.api.register.mapper.RegisterMapper;
import com.classchecks.client.student.api.register.service.RegisterService;
import com.classchecks.client.student.api.register.vo.SecurityAccountVo;
import com.framework.basic.vo.BasicVo;
import com.framework.common.sms.bmob.SMSUtil;
import com.framework.common.util.file.CSVFileUtils;
import com.framework.common.util.file.FileUtils;
import com.framework.common.util.image.SaveImageUtils;
import com.framework.content.code.RegisterBusinessCode;
import com.framework.content.resources.ImgStoragePath;
import com.framework.facerecognizer.recognizer.PreProcessFace;

@Service
public class RegisterServiceImpl implements RegisterService {
	private static Logger LOG = LoggerFactory.getLogger(RegisterServiceImpl.class);
	
	private final static int Student_User_Type = 1; // 学生用户类型
	
	@Autowired
	private RegisterMapper registerMapper;
	@Autowired
	private SMSCodeMapper smsCodeMapper;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public BasicVo register(String phone, String smscode, String regID, CommonsMultipartFile[] files) {
		// 检测数据库是否已有记录，这里检查是防止用户获取验证码成功后，更换一个已有的手机号输入
		boolean hasPhone = smsCodeMapper.hasPhoneRegistered(phone).length > 0 ? true : false;
		if(hasPhone) {
			return new BasicVo(RegisterBusinessCode.BUSSINESS_PHONE_EXIST[0], RegisterBusinessCode.BUSSINESS_PHONE_EXIST[1]);
		}
		
		// 调用短信接口验证输入的短信验证码是否可用
		boolean isVerify = SMSUtil.verifySmsCode(phone, smscode);
		
		if(isVerify) {
			BasicVo basicVo = null;
			try {
				SecurityAccountVo secAcc = new SecurityAccountVo();
				secAcc.setSecurityAccount(phone);
				secAcc.setSecuritSmsCode(smscode);
				secAcc.setRegID(regID);
				secAcc.setSecuritType(Student_User_Type);
//				// 插入数据
				registerMapper.saveRegisterInfo(secAcc);
				secAcc = registerMapper.findAccountByPhone(phone);
				LOG.info("secAcc="+secAcc);
				fileSave(files, phone); // 保存上传的图片到临时位置
				// 图片预处理
				rawFaceProc(ImgStoragePath.RAW_FACE_IMG_SAVE_PATH+File.separator+phone,
						ImgStoragePath.PROC_FACE_IMG_SAVE_PATH+File.separator+phone, secAcc.getFaceLabel());
				// 生成CSV标签
				generateCSV(ImgStoragePath.PROC_FACE_IMG_SAVE_PATH+File.separator+phone, ImgStoragePath.CSV_FILE_SAVE_PATH);
				basicVo = new BasicVo(RegisterBusinessCode.BUSINESS_SUCCESS[0], RegisterBusinessCode.BUSINESS_SUCCESS[1]);
			} catch(Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				basicVo = new BasicVo(RegisterBusinessCode.BUSSINESS_FAILED[0], RegisterBusinessCode.BUSSINESS_FAILED[1]);
				LOG.error("学生注册错误", e);
			}
			return basicVo;
		}
		return new BasicVo(RegisterBusinessCode.BUSSINESS_SMS_ERROR[0], RegisterBusinessCode.BUSSINESS_SMS_ERROR[1]);
	}
	
	/**
	 * 
	* @Title: fileSave 
	* @Description: 对用户上传的图片上进行保存 
	* @param files
	* @param phone
	* void 
	* @throws
	 */
	private void fileSave(CommonsMultipartFile[] files, String phone) {
		String savePath = ImgStoragePath.RAW_FACE_IMG_SAVE_PATH + File.separator + phone;
		for(int i = 0; i < files.length; i ++) {
			SaveImageUtils.saveImage(files[i], savePath, phone+"_"+(i+1)+".jpg");
		}
	}
	
	/**
	 * 
	* @Title: generateCSV 
	* @Description: 根据预处理图像的存储位置生成CSV标签文件 
	* @param rawFacePath - 原始图片的存储路径
	* @param csvFileSavePath 生成的CSV标签文件要保存的路径
	* void 
	* @throws
	 */
	public static void generateCSV(String rawFacePath, String csvFileSavePath) {
		List<String> csvFileList = new ArrayList<String>();
		FileUtils.readFilePathAndMakeCSV(rawFacePath, csvFileList);
		String content = "";
		for(String s : csvFileList) {
			content += s + "\r\n";
		}
		CSVFileUtils.write(csvFileSavePath, content);
	}
	
	/**
	 * 
	* @Title: rawFaceProc 
	* @Description: 用户上传的原始图像的处理
	* @param rawFaceSavePath 原始图像的保存路径
	* @param processedFaceSavePath 经过处理后图像的保存路径
	* @param faceLabel opencv实现人脸识别时人脸标签
	* void 
	 */
	public static boolean rawFaceProc(String rawFaceSavePath, String processedFaceSavePath, int faceLabel) {
		boolean b = PreProcessFace.processRawImage(rawFaceSavePath, processedFaceSavePath, faceLabel);
		return b;
		//System.out.println(b);
	}
}
