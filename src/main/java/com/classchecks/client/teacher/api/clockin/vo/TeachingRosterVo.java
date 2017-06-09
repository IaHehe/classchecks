package com.classchecks.client.teacher.api.clockin.vo;

/**
 * 
 * @ClassName: TeachingRosterVo
 * @Description: 教学班花名册，即学生名单
 * @author Dongjun Zou(984147586@qq.com)
 * @date 2017年5月29日 下午10:32:30
 *
 */
public class TeachingRosterVo {

	public final static Integer CLOCK_IN_OK = 1;
	public final static Integer CLOCK_IN_ABSENCE = 2; // 缺勤
	public final static Integer CLOCK_IN_OTHER = 3;

	private Integer id;
	private String studentName;
	private String stuJWAccount;
	private String loginAccount;
	private Integer stuFaceLabel;
	private String regID; // 极光推送唯一标识
	private String stuClass; // 行政班级
	private Integer clockinType; // 记录考勤类型 1 表示正常、2表示缺勤、3表示其他

	public TeachingRosterVo() {

	}

	public TeachingRosterVo(Integer id, String studentName, String stuJWAccount, String loginAccount,
			Integer stuFaceLabel, String regID, String stuClass, Integer clockinType) {
		super();
		this.id = id;
		this.studentName = studentName;
		this.stuJWAccount = stuJWAccount;
		this.loginAccount = loginAccount;
		this.stuFaceLabel = stuFaceLabel;
		this.regID = regID;
		this.stuClass = stuClass;
		this.clockinType = clockinType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStuJWAccount() {
		return stuJWAccount;
	}

	public void setStuJWAccount(String stuJWAccount) {
		this.stuJWAccount = stuJWAccount;
	}

	public Integer getStuFaceLabel() {
		return stuFaceLabel;
	}

	public void setStuFaceLabel(Integer stuFaceLabel) {
		this.stuFaceLabel = stuFaceLabel;
	}

	public String getRegID() {
		return regID;
	}

	public void setRegID(String regID) {
		this.regID = regID;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public Integer getClockinType() {
		return clockinType;
	}

	public void setClockinType(Integer clockinType) {
		this.clockinType = clockinType;
	}

	public String getStuClass() {
		return stuClass;
	}

	public void setStuClass(String stuClass) {
		this.stuClass = stuClass;
	}

	@Override
	public String toString() {
		return "TeachingRosterVo [id=" + id + ", studentName=" + studentName + ", stuJWAccount=" + stuJWAccount
				+ ", loginAccount=" + loginAccount + ", stuFaceLabel=" + stuFaceLabel + ", regID=" + regID
				+ ", stuClass=" + stuClass + ", clockinType=" + clockinType + "]";
	}

}
