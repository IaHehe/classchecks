package com.jdz.annotation;

import java.io.Serializable;

import com.jdz.annotation.ValueBind.fieldType;

public class Student implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	private String studentId;
	private String name = "";
	private Integer age = 0;

	
	public String getStudentId() {
		return studentId;
	}

	@ValueBind(type=fieldType.STRING, value="10001")
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}
	
	@ValueBind(type=fieldType.STRING, value="hehe")
	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	@ValueBind(type=fieldType.INT, value="20")
	public void setAge(Integer age) {
		this.age = age;
	}

}
