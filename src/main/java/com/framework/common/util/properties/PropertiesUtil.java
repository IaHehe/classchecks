package com.framework.common.util.properties;

import java.io.*;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
* @ClassName: PropertiesUtil 
* @Description: properties文件获取工具类
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年5月11日 上午1:44:42 
*
 */
public class PropertiesUtil {
    private static final Logger logger = Logger.getLogger(PropertiesUtil.class);
    private static Properties props;
    private static String propertiesFile = "";
   /* static{
        try {
			loadProps();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }*/

    synchronized static private void loadProps() {
        
        logger.info("开始加载properties文件内容.......");
        
        props = new Properties();
        InputStream in = null;
        try {
        	// 第一种，通过当前线程类加载器进行获取properties文件流
        	in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile);
        	// 第二种，通过类加载器进行获取properties文件流
            //in = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFile);
            // 第三种，通过类进行获取properties文件流
            //in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("jdbc.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("jdbc.properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    public static void setPropertiesFile(String propsFilePath) {
    	propertiesFile = propsFilePath;
    	loadProps();
    }
    
    public static String getProperty(String key) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
