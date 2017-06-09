package com.framework.common.sms.bmob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.common.sms.bmob.restapi.Bmob;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: SMSUtil 
* @Description: 手机短信相关工具
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年5月10日 下午9:37:43 
*
 */
public class SMSUtil {

	private static final Logger LOG = LoggerFactory.getLogger(SMSUtil.class);
	
    private static final String SMS_TEMPLATE = "重科考勤";

    public static final String REQ_MSG_SUCCESS = "smsId"; // {"smsId":40178394}
    public static final String REQ_MSG_ERROR = "Error"; // {"Error": "...."}
    public static final String REQ_MSG_NOTFOUND = "Not Found"; // {"Not Found":""}
    public static final String SMS_CODE_VERIFY_SUCCESS = "msg";  // {"msg":"ok"}
    
    
    /**
     * @Title: sendSMS
     * @Description: 发送短信
     * @param phone 电话
     */
    public static String sendSMS(String phone) {
        // Bmob初始化 {"smsId":40102638}
        Bmob.initBmob(BmobSmsConfig.getValue("applicationId"), BmobSmsConfig.getValue("restApiKey"));
        
        String result = Bmob.requestSmsCode(phone, SMS_TEMPLATE);
        LOG.info("sendSMS->result=" + result);
        return result;
    }

    /**
     * @Title: verifySmsCode
     * @Description: 验证验证码是否有效；（验证成功一次后再次验证会失败 或者 时间有效期过后验证会失败）
     * @param phone 手机号
     * @param number 验证码
     * @return boolean
     */
    public static boolean verifySmsCode(String phone, String number) {
        Bmob.initBmob(BmobSmsConfig.getValue("applicationId"), BmobSmsConfig.getValue("restApiKey"));
        String result = Bmob.verifySmsCode(phone, number);
        LOG.info("verifySmsCode->result=" + result);
        String jsonKey = (String) JSONObject.fromObject(result).keys().next();
        LOG.info("verifySmsCode->jsonKey="+jsonKey);
        if(SMS_CODE_VERIFY_SUCCESS.equals(jsonKey)) {
        	return true;
        } 
        return false;
    }
   
    
    public static void main(String[] args) {
		String result = sendSMS("17754928703"); //18996316514
		LOG.info("main->result=" + result);
//    	boolean b = verifySmsCode("18302390780", "275887"); // {"msg":"ok"}
//    	System.out.println(b);

	}
}
