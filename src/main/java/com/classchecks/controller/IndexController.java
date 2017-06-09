package com.classchecks.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.classchecks.model.BasicEntity;
import com.classchecks.model.User;
import com.framework.common.util.image.ImageUtils;

@Controller
@RequestMapping("/index")
public class IndexController {
	
	/**
	 * 跳转到json.jsp页面
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping("/do-json")
	public String doPage() {
		return "json";
	}
	
	@RequestMapping("/do-test")
	public String doRegister() {
		return "sys-test";
	}
	
	@RequestMapping("/image-upload")
	public String doImageUpload() {
		return "image-upload";
	}
	
	@RequestMapping("/login")
	public @ResponseBody User Login(@RequestBody User user) {
		User user1 = new User();
		user1.setId(1);
		user1.setUsername("zoudongjun");
		user1.setPassword("123");
		return user1;
	}
	
	@RequestMapping("/do-face-validate")
	public @ResponseBody String image(@RequestParam("imageString") String image) {
		ImageUtils.convertBase64ToImage(image, "D:\\image\\zoudongjun.jpg");
		//System.out.println(image);
		//WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		
		return "face validate is success";
	}
	
	@RequestMapping("/do-upload")
	public @ResponseBody String upload(@RequestParam("file")CommonsMultipartFile file,String username, HttpServletRequest req) {
		String filename = file.getOriginalFilename();
		String path = "D:\\image\\";
		try {
		
			InputStream is = file.getInputStream();
		
			OutputStream os = new FileOutputStream(new File(path,filename));
			
			int len = 0;
			byte [] buffer = new byte[128];
			while((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			is.close();
			os.close();
			
			return "file upload success";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return "file upload fail";
	}
	
	@RequestMapping("/do-detection")
	public @ResponseBody BasicEntity detection(@RequestParam("file")CommonsMultipartFile file,String username, HttpServletRequest req) {
		String filename = file.getOriginalFilename();
		String path = "D:\\image\\";
		
		try {
		
			InputStream is = file.getInputStream();
		
			OutputStream os = new FileOutputStream(new File(path,filename));
			
			int len = 0;
			byte [] buffer = new byte[128];
			while((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			is.close();
			os.close();
			BasicEntity be = new BasicEntity();
			be.setMessage("file upload success");
			return be;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BasicEntity b = new BasicEntity();
		b.setMessage("file upload failed");
		return b;
	}
	
	@RequestMapping("/do-upload-map")
	public @ResponseBody String uploadMap(@RequestParam("file")CommonsMultipartFile file, String username , HttpServletRequest req) {
		String filename = file.getOriginalFilename();
		String path = "D:\\image\\";
		try {
		
			InputStream is = file.getInputStream();
		
			OutputStream os = new FileOutputStream(new File(path,filename));
			
			int len = 0;
			byte [] buffer = new byte[128];
			while((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			is.close();
			os.close();
			return "file upload success";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return "file upload fail";
	}
}




















