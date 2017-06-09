package com.classchecks.client.student.api.register.vo;

public class SecurityAccountVo {

	private Integer id;
	private String securityAccount;
	private String securitSmsCode;
	private Integer faceLabel;
	private String jwAccount;
	private int securitType;
	private String regID; // 极光推送唯一标识, 用于针对某一用户发送推送

	public SecurityAccountVo() {
		super();
	}

	public SecurityAccountVo(Integer id, String securityAccount, String securitSmsCode, Integer faceLabel,
			String jwAccount, int securitType, String regID) {
		super();
		this.id = id;
		this.securityAccount = securityAccount;
		this.securitSmsCode = securitSmsCode;
		this.faceLabel = faceLabel;
		this.jwAccount = jwAccount;
		this.securitType = securitType;
		this.regID = regID;
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

	public String getRegID() {
		return regID;
	}

	public void setRegID(String regID) {
		this.regID = regID;
	}

	@Override
	public String toString() {
		return "SecurityAccountVo [id=" + id + ", securityAccount=" + securityAccount + ", securitSmsCode="
				+ securitSmsCode + ", faceLabel=" + faceLabel + ", jwAccount=" + jwAccount + ", securitType="
				+ securitType + ", regID=" + regID + "]";
	}

}
