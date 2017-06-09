package com.framework.common.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVFileUtils {
	private final static Logger LOG = LoggerFactory.getLogger(CSVFileUtils.class);
	public static boolean write(String CSVFileSavePath, String content) {

		try {

			File file = new File(CSVFileSavePath);
			if(!file.isFile()) {
				file.createNewFile();
			}
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件的指针移到文件尾
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void loadImage(String saveRoute, List<Mat> matLists, List<Integer> labels, Integer faceLabel) {
		File saveRoot = new File(saveRoute);
		if(!saveRoot.exists()) {
			LOG.info("图片保存路径--" + saveRoute + "--不存在");
			return;
		}
		if(!saveRoot.isDirectory()) {
			LOG.info("图片路径--" + saveRoute+ "--不是一个文件夹");
			return;
		}
		
		File[] procImages = saveRoot.listFiles(new ImageFileFilter());
		for(int i = 0; i < procImages.length; i ++) {
			LOG.info("加载图片：" + procImages[i].getAbsolutePath());
			Mat m = Imgcodecs.imread(procImages[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			matLists.add(m);
			labels.add(faceLabel);
		}
	}
	
	public static void loadImage(String saveRoute, List<Mat> matLists, List<Integer> labels) {
		File saveRoot = new File(saveRoute);
		if(!saveRoot.exists()) {
			LOG.info("图片保存路径--" + saveRoute + "--不存在");
			return;
		}
		if(!saveRoot.isDirectory()) {
			LOG.info("图片路径--" + saveRoute+ "--不是一个文件夹");
			return;
		}
		
		File[] procImages = saveRoot.listFiles(new ImageFileFilter());
		for(int i = 0; i < procImages.length; i ++) {
			LOG.info("加载图片：" + procImages[i].getAbsolutePath());
			Mat m = Imgcodecs.imread(procImages[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			matLists.add(m);
			labels.add(Integer.parseInt(procImages[i].getName().split("_")[0]));
		}
	}
	
	public static void loadImage(String saveRoute, List<Mat> matLists) {
		File saveRoot = new File(saveRoute);
		if(!saveRoot.exists()) {
			LOG.info("图片保存路径--" + saveRoute + "--不存在");
			return;
		}
		if(!saveRoot.isDirectory()) {
			LOG.info("图片路径--" + saveRoute+ "--不是一个文件夹");
			return;
		}
		File[] procImages = saveRoot.listFiles(new ImageFileFilter());
		for(int i = 0; i < procImages.length; i ++) {
			//LOG.info("加载图片：" + procImages[i].getAbsolutePath());
			Mat m = Imgcodecs.imread(procImages[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			matLists.add(m);
		}
	}
	
	/**
	 * 读取CSV文件
	 * @param filePath 文件的绝对路径
	 * @param matLists
	 * @param labels
	 */
	public static void CSVRead(String filePath, List<Mat> matLists, List<Integer> labels) {
		try {
			File file = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = br.readLine()) != null) {
				String [] tmp = line.split(";");
				if(!tmp[0].isEmpty() && !tmp[1].isEmpty()) {
					matLists.add(Imgcodecs.imread(tmp[0], Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE));
					labels.add(Integer.valueOf(tmp[1]));
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
