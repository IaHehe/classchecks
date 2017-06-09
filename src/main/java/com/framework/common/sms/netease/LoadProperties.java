package com.framework.common.sms.netease;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
* @ClassName: LoadProperties 
* @Description: 获取网易云信SMS配置属性文件
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年5月11日 上午1:12:58 
*
 */
public class LoadProperties {
	
	private static String AppKey = ""; // 网易云信应用标识Key
	private static String AppSecret = ""; // 秘钥
	
	/**
	 * 获取配置文件db.properties中的jdbc驱动,url信息
	 * 和数据库名、密码
	 */
	static {
		Properties prop = new Properties();
		
		//方法链
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/sms/netease.properties");
		
		try{
			//从输入流中的数据加载成键值对
			prop.load(is);
		} catch(IOException e)
		{
			System.out.println("加载配置文件出错");
		}
		
		//获取driver值
		AppKey = prop.getProperty("AppKey");
		//获取URL值
		AppSecret = prop.getProperty("AppSecret");
	}

	public static String getAppkey() {
		return AppKey;
	}
	
	public static String getAppSecret() {
		return AppSecret;
	}
}
