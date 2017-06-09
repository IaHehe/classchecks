package com.framework.common.sms.bmob;

import java.io.IOException;
import java.util.Properties;


/**
 * 
* @ClassName: BmobSmsConfig 
* @Description: 获取Bmob短信配置参数
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年5月10日 下午9:38:19 
*
 */
public class BmobSmsConfig {

    public BmobSmsConfig() {

    };

    private static Properties props = new Properties();

    static {
        try {
            //props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("sms/bmob.properties"));
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config/sms/bmob.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public static void updateProperties(String key, String value) {
        props.setProperty(key, value);
    }
}
