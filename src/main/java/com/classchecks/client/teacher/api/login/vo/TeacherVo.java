package com.classchecks.client.teacher.api.login.vo;

public class TeacherVo {

	private Integer id;
	private String securityAccount;
	private String securitSmsCode;
	private Integer faceLabel;
	private String jwAccount;
	private int securitType;

	public TeacherVo() {
		super();
	}

	public TeacherVo(Integer id, String securityAccount, String securitSmsCode, Integer faceLabel, String jwAccount,
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
		return "StudentVo [id=" + id + ", securityAccount=" + securityAccount + ", securitSmsCode=" + securitSmsCode
				+ ", faceLabel=" + faceLabel + ", jwAccount=" + jwAccount + ", securitType=" + securitType + "]";
	}

}
