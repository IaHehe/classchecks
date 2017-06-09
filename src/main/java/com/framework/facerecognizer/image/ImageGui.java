package com.framework.facerecognizer.image;

import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by kofee on 2016/3/28.
 */
public class ImageGui {
	private JLabel imageView;

	private Mat image;
	private String windowName;
	private static boolean isShow = false;
	/**
	 * 如果使用junit测试时调用该方法，图像会一闪而过，可通过sleep()等方式暂时显示
	 *
	 * @param
	 */

	private ImageGui(Mat image) {
		this.image = image;
	}

	/**
	 * @param image
	 *            要显示的mat
	 * @param windowName
	 *            窗口标题
	 */
	private ImageGui(Mat image, String windowName) {
		this.image = image;
		this.windowName = windowName;
	}
	
	public static void imshow(Mat image, String windowName) {
		
		if(isShow) {
			new ImageGui(image, windowName).imshow();
		}
	}
	
	/**
      * 图片显示
      */
     private void imshow() {
         setSystemLookAndFeel();
        Image loadedImage = toBufferedImage(image);
         JFrame frame = createJFrame(windowName, image.width(), image.height());
        imageView.setIcon(new ImageIcon(loadedImage));
         frame.pack();
         frame.setLocationRelativeTo(null);
         frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 用户点击窗口关闭
    }

	private void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private JFrame createJFrame(String windowName, int width, int height) {
		JFrame frame = new JFrame(windowName);
		imageView = new JLabel();
		final JScrollPane imageScrollPane = new JScrollPane(imageView);
		imageScrollPane.setPreferredSize(new Dimension(width, height));
		frame.add(imageScrollPane, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		return frame;
	}

	private Image toBufferedImage(Mat matrix) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (matrix.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
		byte[] buffer = new byte[bufferSize];
		matrix.get(0, 0, buffer); // 获取所有的像素点
		
		BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		return image;
	}
}
