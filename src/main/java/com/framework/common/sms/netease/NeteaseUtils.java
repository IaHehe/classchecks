package com.framework.common.sms.netease;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import com.framework.common.util.randrom.RandomUtils;

/**
 * 
* @ClassName: NeteaseSMSUtils 
* @Description: 网易云信短信工具类
* @author Dongjun Zou(984147586@qq.com) 
* @date 2017年5月11日 上午1:43:58 
*
 */
public class NeteaseUtils {
	
	private static String STRING_EMPTY = "";
	
	private static boolean IS_INIT = false;
	private static int TIME_OUT = 10000;
	// app key (必要)
	private static String APP_KEY = STRING_EMPTY; 
	// 秘钥 (必要)
	private static String APP_SECRET = STRING_EMPTY; 
	// 随机数 (必要)
	private static String REQUIRED_NONCE = STRING_EMPTY;
	// 从1970年1月1日0点0 分0 秒开始到现在的秒数 (必要)
	private static String REQUIRED_CURTIME = STRING_EMPTY; 
	// SHA1(AppSecret + Nonce + CurTime),三个参数拼接的字符串，进行SHA1哈希计算，转化成16进制字符(String，小写)
	private static String REQUIRED_CHECKSUM = STRING_EMPTY; 
	// 短信模板ID，用于短信签名 (非必要 默认显示【云短信】)
	private static int TEMPLATE_ID = 0;
	// 验证码长度 (非必要 默认4位)
	private static int CODE_LEN = 0;
	
	// 网易云信所有请求必须POST
	private final static String METHOD_POST = "POST";
	// 所有接口返回类型为JSON，同时进行UTF-8编码。
	private final static String ENCODING_UTF8 = "UTF-8";
	 // 所有接口请求Content-Type类型为：application/x-www-form-urlencoded;charset=utf-8；
	private final static String CONTENT_TYPE_TAG = "Content-Type";
	private final static String CONTENT_TYPE_APPCATION = "application/x-www-form-urlencoded";
	
	public static boolean isInit() {
		return IS_INIT;
	}

	/**
	 * 
	* @Title: initAppInfo 
	* @Description: 初始化网易云信申请的短信应用的APPKey和APPSecret(秘钥)
	* @param appKey
	* @param appSecret
	* @return
	* boolean 
	* @throws
	 */
	public static boolean initAppInfo(String appKey, String appSecret, int templateid, int codeLen) {
		return initAppInfo(appKey, appSecret, templateid, codeLen, 5000);
	}

	/**
     * 初始化SMS工具类
     * 
     * @param appKey 填写 应用ID
     * @param appSecret 填写 应用秘钥
     * @param timeout 设置超时（1000~20000ms）
     * @return 注册结果
     */
    public static boolean initAppInfo(String appId, String apiKey,int templateid, int codeLen, int timeout) {
    	APP_KEY = appId;
    	APP_SECRET = apiKey;
        if (!APP_KEY.equals(STRING_EMPTY) && !APP_SECRET.equals(STRING_EMPTY)) {
            IS_INIT = true;
        }
        
        TEMPLATE_ID = templateid;
        CODE_LEN = codeLen;
        if(CODE_LEN > 4 && CODE_LEN < 10) {
        	CODE_LEN = codeLen;
        }
        
        if (timeout > 1000 && timeout < 20000) {
            TIME_OUT = timeout;
        }
        
        REQUIRED_NONCE = RandomUtils.generateNumeric(6);
        REQUIRED_CURTIME = calcCurTime();
        REQUIRED_CHECKSUM = CheckSumBuilder.getCheckSum(APP_SECRET, REQUIRED_NONCE, REQUIRED_CURTIME);
        
        return isInit();
    }
    
    
    /**
     * 
    * @Title: sendSMSCode 
    * @Description: 发送验证码
    * @param mobile
    * @return
    * String 
    * @throws
     */
    public static String post(Map<String, String> queryParams, String reqURL) {
    	HttpURLConnection conn = null;
    	try {
			URL mURL = new URL(reqURL);
			conn = getHttpConnection(conn, mURL, METHOD_POST);
			conn.connect();
			
			String data = resolveRequestParams(queryParams);
			
			OutputStream out = conn.getOutputStream();
            out.write(data.getBytes(ENCODING_UTF8));
            out.flush();
            out.close();
            
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    	return readResponseString(conn);
    }
    
    
    
    /**
     * 根据网易云信的接口要求，传入的UTC时间戳必须是从1970年1月1日0点0 分0 秒开始到现在的秒数(String)
    * @Title: calcCurTime 
    * @Description: 当前UTC时间戳，从1970年1月1日0点0 分0 秒开始到现在的秒数(String) 
    * @return
    * String 当前时间的字符串
     */
    private static String calcCurTime() {
    	String curTime = String.valueOf((new Date()).getTime() / 1000L);
    	return curTime;
    }
	
    private static String resolveRequestParams(Map<String, String> queryParams) {
    	StringBuilder sb = new StringBuilder();
    	for (Map.Entry<String, String> entry : queryParams.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();
            if (null != value)
                try {
                    value = URLEncoder.encode(value, ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            sb.append(key).append("=").append(value);
            sb.append("&");
        }
    	return sb.toString();
    }
    
    private static HttpURLConnection getHttpConnection(HttpURLConnection conn, URL url, String method) throws IOException {
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setReadTimeout(TIME_OUT);

        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);

        conn.setRequestProperty("AppKey", APP_KEY);
        conn.setRequestProperty("CurTime", REQUIRED_CURTIME);
        conn.setRequestProperty("Nonce", REQUIRED_NONCE);
        conn.setRequestProperty("CheckSum", REQUIRED_CHECKSUM);
        conn.setRequestProperty(CONTENT_TYPE_TAG, CONTENT_TYPE_APPCATION);
        
        if(CODE_LEN > 4) {
        	conn.setRequestProperty("codeLen", "6");
        }
        if(TEMPLATE_ID != 0) {
        	conn.setRequestProperty("", "");
        }

        return conn;
    }
    
    private static String readResponseString(HttpURLConnection conn) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING_UTF8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
