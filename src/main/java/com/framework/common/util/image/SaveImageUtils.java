package com.framework.common.util.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.framework.common.util.file.FileUtils;

public class SaveImageUtils {
	private static Logger LOG = LoggerFactory.getLogger(SaveImageUtils.class);
	
	static String[] fileType = { "application/octet-stream", "image/png", "image/jpg", "image/jpeg", "image/gif", "image/bmp" }; // 限制文件上传类型
	
	/**
	 * 
	* @Title: saveImage 
	* @Description: TODO(保存图片到指定的路径) 
	* @param file 上传的图片文件
	* @param savePath 指定文件保存路径
	* @param filename 文件名
	* @return
	* boolean 
	 */
	public static boolean saveImage(MultipartFile file, String savePath, String filename) {
	
		try {
			
			// 检查保存路径是否存在，不存在则创建文件夹
			FileUtils.isExist(savePath);
			// 获取文件的contentType
			String fileContentType = file.getContentType();
			if(isAllow(fileContentType)) {
				file.transferTo(new File(savePath, filename));
				return true;
			}
		} catch (FileNotFoundException e) {
			LOG.info("图片保存失败", e);
			return false;
		} catch (IOException e) {
			LOG.info("图片保存异常", e);
			return false;
		}
		
		return false;
	}
	
	/**
	 * 
	* @Title: isAllow 
	* @Description: TODO(文件类型是否被允许) 
	* @param suffix
	* @return
	* boolean 
	 */
	public static boolean isAllow(String suffix) {
		for(String s: fileType) {
			if(s.toLowerCase().equals(suffix.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
