package com.framework.common.util.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileUtils {
	
	/**
	 * inputStream 转换成String
	 * @param in
	 * @return
	 * @throws IOException 
	 */
	public static String inputStream2String(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = in.read()) != -1) {
            baos.write(i);
        }
        return new String(baos.toByteArray());
	}
	
	/**
	 * 
	* @Title: getFileName 
	* @Description: 获取单个文件夹中的文件名
	* @param path
	* @return
	* String[] 
	* @throws IOException
	 */
	public static String [] getFileName(String fileDirPath) throws IOException {
		File file = new File(fileDirPath);
		if(!file.isDirectory()) {
			throw new IOException("fileDirPath 不是一个文件夹路径");
		}
		String [] names = file.list();
		return names;
	}
	
	/**
	 * 以文件路径下的所有图片文件生成CSV便签文件
	 * <p>  逗号分隔值（Comma-Separated Values，CSV，有时也称为字符分隔值，因为分隔字符也可以不是逗号），
	 * 其文件以纯文本形式存储表格数据（数字和文本）。纯文本意味着该文件是一个字符序列，不含必须像二进制数
	 * 字那样被解读的数据。CSV文件由任意数目的记录组成，记录间以某种换行符分隔；每条记录由字段组成，字段间
	 * 的分隔符是其它字符或字符串，最常见的是逗号或制表符。通常，所有记录都有完全相同的字段序列。<p>
	 * @param path 需要读取的文件保存的路径
	 * @param csvFileList 用于接收生成的csv文件，调用处需要初始化一个ArrayList对象用于接收
	 */
	public static void readFilePathAndMakeCSV(String path, List<String> csvFileList)
    {
        File file = new File(path);
        
        if(file.isDirectory()) {
        	String [] names = file.list(new ImageFileFilter());
            if(names != null) {
    			for(int i = 0; i < names.length; i ++) {
    				csvFileList.add(file.toString() + "\\" + names[i] + ";" + names[i].split("\\.")[0].split("\\_")[0]);
    			}
            }
        }
        File [] files = file.listFiles();
        for(File a:files)
        {
            if(a.isDirectory())
            {
            	readFilePathAndMakeCSV(a.getAbsolutePath(),csvFileList);
            }
        }
    }
	
	public static void print(File file) {	// 递归调用
		if(file!=null){	// 判断对象是否为空
			if(file.isDirectory()){	// 如果是目录
				File f[] = file.listFiles();	// 列出全部的文件
				if(f!=null){	// 判断此目录能否列出
					for(int i=0;i<f.length;i++) {
						print(f[i]) ;	// 因为给的路径有可能是目录，所以，继续判断
					}
				}
			}else{
				//System.out.println(file.getParentFile().getName());
				String fileName = file.getName();
				if(fileName.endsWith(".jpg") || fileName.endsWith(".pgm") || fileName.endsWith(".png")) {
					System.out.println(file);
				}
			}
		}
	}
	
	/**
	 * 
	* @Title: isExist 
	* @Description: 判断文件夹是否存在，如果不存在则创建文件夹 
	* @param path
	* void 
	 */
	public static void isExist(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	 /**
     * 删除文件，可以是单个文件或文件夹
     * 
     * @param fileName 待删除的文件名
     * @return 文件删除成功返回true,否则返回false
     */

    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     * 
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true,否则返回false
     */

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * 
     * @param dir 被删除目录的文件路径
     * @return 目录删除成功返回true,否则返回false
     */

    public static boolean deleteDirectory(String dir) {

        // 如果dir不以文件分隔符结尾，自动添加文件分隔符

        if (!dir.endsWith(File.separator)) {

            dir = dir + File.separator;

        }

        File dirFile = new File(dir);

        // 如果dir对应的文件不存在，或者不是一个目录，则退出

        if (!dirFile.exists() || !dirFile.isDirectory()) {

            System.out.println("删除目录失败" + dir + "目录不存在！");

            return false;

        }

        boolean flag = true;

        // 删除文件夹下的所有文件(包括子目录)

        File[] files = dirFile.listFiles();

        for (int i = 0; i < files.length; i++) {

            // 删除子文件

            if (files[i].isFile()) {

                flag = deleteFile(files[i].getAbsolutePath());

                if (!flag) {

                    break;

                }

            }

            // 删除子目录

            else {

                flag = deleteDirectory(files[i].getAbsolutePath());

                if (!flag) {

                    break;

                }

            }

        }

        if (!flag) {

            System.out.println("删除目录失败");

            return false;

        }

        // 删除当前目录

        if (dirFile.delete()) {

            System.out.println("删除目录" + dir + "成功！");

            return true;

        } else {

            System.out.println("删除目录" + dir + "失败！");

            return false;

        }

    }
    
    /**
     * 
    * @Title: buildFilePath 
    * @Description: 根据传入的key生成文件路径，例如key={"d:/text","tmp","123"}生成为"d:\tmp\123"
    * <p>使用时注意key的先后顺序</p>
    * @param key
    * @return
    * String 
    * @throws
     */ 
    public static String buildFilePath(String ... key) {
    	StringBuffer sb = new StringBuffer();
    	for(String s : key) {
    		s = s.replace("/", "\\");
    		sb.append(s).append("\\");
    	}
    	
    	if(sb.length() > 0 && sb.charAt(sb.length()-1) == '\\') {
    		sb.deleteCharAt(sb.length()-1);
    	}
    	
    	return sb.toString();
    }

    public static void main(String[] args) {
		String [] key = {"d:/text","tmp","123"};
		System.out.println(buildFilePath(key));//"d:/df","1","10"
		
	}
    
}
