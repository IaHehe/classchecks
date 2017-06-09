package com.framework.common.util.file;

import java.io.File;
import java.io.FilenameFilter;

/**
* @ClassName: ImageFileFilter 
* @Description: 过滤图片文件，根据文件后缀名
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年4月15日 下午4:42:36 
*
 */
public class ImageFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		name = name.toLowerCase();
		return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
	}

}
