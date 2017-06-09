package com.framework.common.util;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;

import com.classchecks.client.global.api.jpush.vo.JPushByTeacherVo;
import com.classchecks.client.teacher.api.clockin.vo.TeachingRosterVo;
import com.framework.basic.vo.BasicEntityListVo;
import com.framework.basic.vo.BasicEntityVo;
import com.google.gson.reflect.TypeToken;

public class GsonUtilTest {

	@Test
	public void test() {
		String json = "[{\"teacherName\":\"伍建全\",\"teacherRegId\":\"123423423\",\"courseName\":\"雅思六级\","
				+ "\"startSection\":7,\"weekday\":14},{\"teacherName\":\"伍建全\",\"teacherRegId\":\"100d8559097725d211b\","
				+ "\"courseName\":\"雅思六级\",\"startSection\":7,\"weekday\":14}]";
	
		List<JPushByTeacherVo> vo = GsonUtil.jsonToList(json, JPushByTeacherVo.class);
		//System.out.println(vo.get(0).getTeacherName());
		System.out.println(vo.get(0).getCourseName());
		System.out.println(vo.toString());
	}
	
	@Test
	public void testJson() {
		String json = "{\"code\":\"4000\",\"message\":\"考勤操作成功\",\"dataList\":[{\"id\":1053,\"stuName\":\"李灵黛\",\"stuJWAccount\":\"2013440001\",\"loginAccount\":\"13200000001\",\"stuFaceLabel\":45,\"regID\":\"100d8559097725d211b\",\"clockinType\":2}]}";
	
		Type jsonType = new TypeToken<BasicEntityListVo<TeachingRosterVo>>() {  
	    }.getType();
		BasicEntityListVo<TeachingRosterVo> bel = GsonUtil.GsonToBean(json, BasicEntityListVo.class, jsonType);
		
		System.out.println(bel.getDataList().get(0).getLoginAccount());
	}

}
