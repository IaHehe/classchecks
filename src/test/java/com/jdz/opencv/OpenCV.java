package com.jdz.opencv;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCV {
	public static void main(String[] args) {
		String path = "G:\\java\\JavaProjectRelease\\classchecks\\src\\main\\webapp\\WEB-INF\\dll\\x64\\opencv_java320.dll";
        System.load(path);
    	Mat m = new Mat(128,128, CvType.CV_8UC1);
    	System.out.println(m.width());
		
	}
	
	private void addDirToPath(String s) {

		try {
			// 获取系统path变量对象
			Field field = ClassLoader.class.getDeclaredField("sys_paths");
			// 设置此变量对象的值
			field.setAccessible(true);
			// 获取此变量的值
			String[] path = (String[]) field.get(null);
			// 创建字符串数组，在原来的数组长度上增加一个，用于存放增加的目录
			String[] tmp = new String[path.length + 1];
			// 将原来的path变量复制到tem中
			System.arraycopy(path, 0, tmp, 0, path.length);
			// 将原来的目录存入新的变量数组中
			tmp[path.length] = s;
			System.out.println(Arrays.toString(tmp));
			// 将增加目录后的数组赋给path变量对象
			field.set(null, tmp);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static void addLibraryDir(String libraryPath) throws Exception {  
        Field userPathsField = ClassLoader.class.getDeclaredField("usr_paths");  
        userPathsField.setAccessible(true);  
        String[] paths = (String[]) userPathsField.get(null);  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < paths.length; i++) {  
            if (libraryPath.equals(paths[i])) {  
                continue;  
            }  
            sb.append(paths[i]).append(';');  
        }  
        sb.append(libraryPath);  
        System.setProperty("java.library.path", sb.toString());  
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");  
        sysPathsField.setAccessible(true);  
        sysPathsField.set(null, null);  
    } 
}
