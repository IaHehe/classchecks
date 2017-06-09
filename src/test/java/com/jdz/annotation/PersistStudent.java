package com.jdz.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PersistStudent {
	public static void main(String[] args) {
		try {
			Object c = Class.forName("com.jdz.annotation.Student").newInstance();
			
			Method[] methodArray = c.getClass().getDeclaredMethods();
			
			for(int i = 0; i < methodArray.length; i ++) {
				if(methodArray[i].isAnnotationPresent(ValueBind.class)) {
					ValueBind vb = methodArray[i].getAnnotation(ValueBind.class);
					String type = String.valueOf(vb.type());
					String value = vb.value();
					if(type.equals("INT")) {
						Integer [] integer = new Integer [] {Integer.valueOf(value)};
						methodArray[i].invoke(c, integer);
					} else {
						methodArray[i].invoke(c,  new String[] {value});
					}
				}
			}
			Student s = (Student) c;
			System.out.println("studentId:"+s.getStudentId() + ", studentName:" + s.getName()
					+ ", studentAge:" + s.getAge());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
