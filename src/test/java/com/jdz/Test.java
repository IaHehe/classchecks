package com.jdz;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.framework.basic.vo.BasicEntityVo;
import com.framework.common.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class Test {

	public static void main(String[] args) {
		String str = "{\"code\":\"2000\",\"message\":\"登录成功\",\"data\":{\"id\":44,\"securityAccount\":\"15823789581\",\"securitSmsCode\":\"224693\",\"faceLabel\":44,\"jwAccount\":null,\"securitType\":0}}";
		BasicEntityVo<TeacherBean> bev = GsonUtil.GsonToBean(str, BasicEntityVo.class);
		
		String strJosn = GsonUtil.GsonString(bev.getData());
		TeacherBean t = GsonUtil.GsonToBean(strJosn, TeacherBean.class);
		System.out.println(t);
		
//		Gson gson = new Gson();
	    Type jsonType = new TypeToken<BasicEntityVo<TeacherBean>>() {  
	    }.getType(); 
		
		BasicEntityVo<TeacherBean> vo = GsonUtil.GsonToBean(str, BasicEntityVo.class, jsonType);
		System.out.println(vo.getData());
	}
	
	public static void main1(String[] args) {
		/*
		 * Calendar mCalendar = Calendar.getInstance();
		 * mCalendar.setTimeZone(TimeZone.getDefault());
		 * 
		 * int week = mCalendar.get(Calendar.WEEK_OF_YEAR);
		 * System.out.println(week);
		 */

		/*int x = 190;
		double y =  (((x+52.8) * 5 - 3.9343) / 0.5 - (x * 10));
		System.out.println(y);*/
		
		/*String json = "{\"api_code\":\"0\",\"api_body\":{year:2016,semester:2,curweek:13," + "courseData:["
				+ "{day:4,content:形势与政策Ⅳ,teacher:曾瑜,classRoom:博学楼E216,startSection:3,endSection:4,weekStart:0,weekEnd:0},"
				+ "{day:3,content:体育Ⅱ(篮球),teacher:张翼,classRoom:T001风雨球场,startSection:3,endSection:4,weekStart:0,weekEnd:0}]}"
				+ ",\"api_message\":\"成功\"}";

		CourseBean bean = GsonUtil.GsonToBean(json, CourseBean.class);
		System.out.println(bean);*/
		String path = "d:/img/dr%20vier/ni%20hao";
		System.out.println(path.replace("%20", " "));
	}

}

class CourseBean {

	private String api_code;
	private ApiBody api_body;
	private String api_message;

	public CourseBean() {
	}

	public CourseBean(String api_code, ApiBody api_body, String api_message) {
		this.api_code = api_code;
		this.api_body = api_body;
		this.api_message = api_message;
	}

	public String getApi_code() {
		return api_code;
	}

	public void setApi_code(String api_code) {
		this.api_code = api_code;
	}

	public ApiBody getApi_body() {
		return api_body;
	}

	public void setApi_body(ApiBody api_body) {
		this.api_body = api_body;
	}

	public String getApi_message() {
		return api_message;
	}

	public void setApi_message(String api_message) {
		this.api_message = api_message;
	}
	
	
	
	@Override
	public String toString() {
		return "CourseBean [api_code=" + api_code + ", api_body=" + api_body + ", api_message=" + api_message + "]";
	}



	public class ApiBody {
		private String year;
		private String semester;
		private List<SimpleSection> courseData;
		private String curWeek;

		public ApiBody() {
		}

		public ApiBody(String year, String semester, List<SimpleSection> courseData, String curWeek) {
			this.year = year;
			this.semester = semester;
			this.courseData = courseData;
			this.curWeek = curWeek;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public List<SimpleSection> getCourseData() {
			return courseData;
		}

		public void setCourseData(List<SimpleSection> courseData) {
			this.courseData = courseData;
		}

		public String getCurWeek() {
			return curWeek;
		}

		public void setCurWeek(String curWeek) {
			this.curWeek = curWeek;
		}

		@Override
		public String toString() {
			return "ApiBody [year=" + year + ", semester=" + semester + ", courseData=" + courseData + ", curWeek="
					+ curWeek + "]";
		}
	}
}

class SimpleSection {

	int id; // 课程ID
	int day; // 周几
	String content; // 课程名称
	String teacher; // 任课教师
	String classRoom; // 教室
	int startSection;// 开始节
	int endSection;// 结束节
	int weekStart;// 开始周次
	int weekEnd;// 结束周次

	public SimpleSection() {
	}

	public SimpleSection(int id, int day, String content, String teacher, String classRoom, int startSection,
			int endSection) {
		this.id = id;
		this.day = day;
		this.content = content;
		this.teacher = teacher;
		this.classRoom = classRoom;
		this.startSection = startSection;
		this.endSection = endSection;
	}

	public SimpleSection(int id, int day, String content, String teacher, String classRoom, int startSection,
			int endSection, int weekStart, int weekEnd) {
		this.id = id;
		this.day = day;
		this.content = content;
		this.teacher = teacher;
		this.classRoom = classRoom;
		this.startSection = startSection;
		this.endSection = endSection;
		this.weekStart = weekStart;
		this.weekEnd = weekEnd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}

	public int getStartSection() {
		return startSection;
	}

	public void setStartSection(int startSection) {
		this.startSection = startSection;
	}

	public int getEndSection() {
		return endSection;
	}

	public void setEndSection(int endSection) {
		this.endSection = endSection;
	}

	public int getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(int weekStart) {
		this.weekStart = weekStart;
	}

	public int getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd(int weekEnd) {
		this.weekEnd = weekEnd;
	}

	@Override
	public String toString() {
		return "SimpleSection{" + "id=" + id + ", day=" + day + ", content='" + content + '\'' + ", teacher='" + teacher
				+ '\'' + ", classRoom='" + classRoom + '\'' + ", startSection=" + startSection + ", endSection="
				+ endSection + ", weekStart=" + weekStart + ", weekEnd=" + weekEnd + '}';
	}

}



class TeacherBean {

    private Integer id;
    private String securityAccount;
    private String securitSmsCode;
    private Integer faceLabel;
    private String jwAccount;
    private int securitType;

    public TeacherBean() {
        super();
    }

    public TeacherBean(Integer id, String securityAccount, String securitSmsCode, Integer faceLabel, String jwAccount,
                       int securitType) {
        super();
        this.id = id;
        this.securityAccount = securityAccount;
        this.securitSmsCode = securitSmsCode;
        this.faceLabel = faceLabel;
        this.jwAccount = jwAccount;
        this.securitType = securitType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecurityAccount() {
        return securityAccount;
    }

    public void setSecurityAccount(String securityAccount) {
        this.securityAccount = securityAccount;
    }

    public String getSecuritSmsCode() {
        return securitSmsCode;
    }

    public void setSecuritSmsCode(String securitSmsCode) {
        this.securitSmsCode = securitSmsCode;
    }

    public Integer getFaceLabel() {
        return faceLabel;
    }

    public void setFaceLabel(Integer faceLabel) {
        this.faceLabel = faceLabel;
    }

    public String getJwAccount() {
        return jwAccount;
    }

    public void setJwAccount(String jwAccount) {
        this.jwAccount = jwAccount;
    }

    public int getSecuritType() {
        return securitType;
    }

    public void setSecuritType(int securitType) {
        this.securitType = securitType;
    }

	@Override
	public String toString() {
		return "TeacherBean [id=" + id + ", securityAccount=" + securityAccount + ", securitSmsCode=" + securitSmsCode
				+ ", faceLabel=" + faceLabel + ", jwAccount=" + jwAccount + ", securitType=" + securitType + "]";
	}
}