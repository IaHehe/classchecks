package com.framework.common.util.jpush;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.common.util.properties.PropertiesUtil;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NativeHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;



public class JPushUtils {

	protected static final Logger LOG = LoggerFactory.getLogger(JPushUtils.class);
	
	private static String appKey = "";
	private static String masterSecret = "";
	private static String tagStudent = "";
	private static String tagTeacher = "";
	
	private static int ANDROID_TAG_STUDENT = 1;
	private static int ANDROID_TAG_TEACHER = 2;
	
	static {
		
		PropertiesUtil.setPropertiesFile("config/jpush/jpush.conf");
		appKey = PropertiesUtil.getProperty("appKey");
		masterSecret = PropertiesUtil.getProperty("masterSecret");
		tagStudent = PropertiesUtil.getProperty("tagStudent");
		tagTeacher = PropertiesUtil.getProperty("tagTeacher");
		
	}
	
	public static PushResult sendPushByAndroidTag(int tag, String pushAlert, String pushTitle) {
		
		ClientConfig clientConfig = ClientConfig.getInstance();
		final JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        NativeHttpClient httpClient = new NativeHttpClient(authCode, null, clientConfig);
        jpushClient.getPushClient().setHttpClient(httpClient);
		final PushPayload payload;
		
        if(tag == 1) {
        	 payload = buildPushObject_android_tags_alert(tagStudent, pushAlert, pushTitle);
        } else {
        	payload = buildPushObject_android_tags_alert(tagTeacher, pushAlert, pushTitle);
        }
        
        try {
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);
			return result;
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());
			e.printStackTrace();
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
			e.printStackTrace();
		}
        
        return null;
	}
	
	public static PushResult sendPushByAndroidRegId(String pushAlert, String pushTitle, List<String> regIds) {
		ClientConfig clientConfig = ClientConfig.getInstance();
		final JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        NativeHttpClient httpClient = new NativeHttpClient(authCode, null, clientConfig);
        jpushClient.getPushClient().setHttpClient(httpClient);
        
        PushPayload payload = buildPushObject_android_regId_alert(pushAlert, pushTitle, regIds);
        
        try {
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);
			return result;
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
            LOG.error("Sendno: " + payload.getSendno());
			e.printStackTrace();
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            LOG.error("Sendno: " + payload.getSendno());
			e.printStackTrace();
		}
        return null;
	}
	
	/**
     * 
    * @Title: buildPushObject_all_tags_teacher_alert 
    * @Description: TODO(学生端) 
    * @return
    * PushPayload 
     */
	private static PushPayload buildPushObject_android_tags_alert(String tag, String pushAlert, String pushTitle) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())//设置接受的平台
                .setAudience(Audience.tag(tag))//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.newBuilder()
                		.setAlert(pushAlert)
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle(pushTitle).build())
                		.build())
                .build();
    }
	
    private static PushPayload buildPushObject_android_regId_alert(String pushAlert, String pushTitle, List<String> regIds) {
    	
    	Map<String, String> extras = new HashMap<String, String>();
        extras.put("extra_1", "val1");
        extras.put("extra_2", "val2");
    	
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.android())
    			.setAudience(Audience.registrationId(regIds))
    			.setNotification(Notification
    					.newBuilder()
    					.setAlert(pushAlert)
    					.addPlatformNotification(AndroidNotification.newBuilder()
    							.setTitle(pushTitle)
    							.addExtras(extras)
    							.build())
    					.build())
    			.build();
    }
    
	public static void main(String[] args) {
//		LOG.info("appkey:" + appKey + "masterSecret:" + masterSecret
//				+ "tagStudent:" + tagStudent + "tagTeacher:" + tagTeacher);
		
		String alert = "当前考勤没有识别到你，请打开软件自己考勤"
				+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
//		String stuentTitle = "考勤提醒";
//		sendPushByAndroidTag(ANDROID_TAG_STUDENT, alert, stuentTitle);
		
		//String alertTeacher = "";
//		String teacherTitle = "考勤提醒";
//		sendPushByAndroidTag(ANDROID_TAG_TEACHER, alert, teacherTitle);
		//sendPushByAndroidRegId("100d8559097725d211b");
	}
	
	
}
