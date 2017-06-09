package com.framework.common.util.image;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageUtils {
	
	
	/**
	 *  将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * @param path	图片路径
	 * @return
	 */
	public static String covertImageToBase64(String path) {
		byte [] buf = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(path);
			buf = new byte[in.available()];
			in.read(buf);
			in.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		// 对字节数组进行Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(buf); // 返回Base64编码字符串
	} 
	
	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * @param base64	 图像的Base64编码字符串
	 * @param fileSavePath	图像要保存的地址
	 * @return
	 */
	public static boolean convertBase64ToImage(String base64, String fileSavePath) {
		if(base64 == null) return false;
		
		BASE64Decoder decoder = new BASE64Decoder();
		
		// Base64解码
		try {
			byte[] buffer = decoder.decodeBuffer(base64);
			for(int i = 0; i < buffer.length; i ++) {
				if(buffer[i] < 0) {
					buffer[i] += 256;
				}
			}
			// 生成JPEG图片
			OutputStream out = new FileOutputStream(fileSavePath);
			out.write(buffer);
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
