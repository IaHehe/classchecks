package com.classchecks.client.global.api.sms.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



import com.framework.basic.vo.BasicVo;
import com.framework.common.util.GsonUtil;
import com.framework.common.util.position.Gps;
import com.framework.common.util.position.PositionUtil;

import junit.JUnitTestBase;
import net.sf.json.JSONObject;

public class SMSCodeServiceTest {// extends JUnitTestBase {

	@Autowired
	private SMSCodeService smsCodeService;
	
	@Test
	public void testRequestSMSCode() {
		BasicVo basicVo = smsCodeService.registerSMSCode("18302390780");
		System.out.println(basicVo);
	}
	
	@Test
	public void testGsonUtil() {
		//String result = "{\"code\": \"dfdfs\"}";
		String result = "{\"code\":\"3000\",\"message\":\"验证码获取成功\"}";
		BasicVo b = GsonUtil.GsonToBean(result, BasicVo.class);
		System.out.println(b);
//		System.out.println(JSONObject.fromObject(result).keys().next());
	}
	
	@Test
	public void testPositionUtil() {
		//Gps gps = PositionUtil.gps84_To_Gcj02(29.59571861, 106.31904841); // 
		Gps gps = PositionUtil.gps84_To_Gcj02(29.59583, 106.318995); //29.593203665023594,106.32286556704403

		System.out.println(gps); 
		// 
		/**
		 * 数据来自：http://www.gpsspg.com/
		 * 29.5951164617403,106.32291615539995
		 * 谷歌地图：29.5930972201,106.3229161554
			百度地图：29.5994101473,106.3293245374
			腾讯高德：29.5930992400,106.3229276900
			图吧地图：29.5928786100,106.3160684100
			谷歌地球：29.5957186100,106.3190484100
			北纬N29°35′44.59″ 东经E106°19′8.57″
			海拔：296.90 米
			靠近：重庆市沙坪坝区大学城东路21号
			参考：重庆市沙坪坝区虎溪街道虎兴东南方向约1.11公里
		 */
	}
	
	@Test
	public void testLngLatConvert() {
		LatLng ll = pianyi(29.59571861, 106.31904841);
		System.out.println(ll); // 29.602128655582547,106.32509094557331
	}
	
	
	private LatLng pianyi(double lon,double lat)
    {
        double x = lon; double y = lat;
        double z = Math.sqrt(x*x+y*y) + 0.00002 *Math.sin(y*Math.PI) ;
        double temp =Math.atan2(y, x)  + 0.000003 * Math.cos(x*Math.PI);

        double bdLon = z * Math.cos(temp) + 0.0065;
        double bdLat = z * Math.sin(temp) + 0.006;
        LatLng newcenpt = new LatLng(bdLat, bdLon);
        return newcenpt;
    }
	
	private class LatLng {
		public double bdLat;
		public double bdLon;
		public LatLng(double bdLat, double bdLon) {
			this.bdLat = bdLat;
			this.bdLon = bdLon;
		}
		@Override
		public String toString() {
			// "LatLng [bdLat=" + bdLat + ", bdLon=" + bdLon + "]";
			return bdLon+","+bdLat;
		}
		
	}
	
	
}
