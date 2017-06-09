package com.framework.basic.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.annotation.PostConstruct;  
import javax.annotation.PreDestroy;

import org.opencv.core.Core; 


public class PostConstructService {

	@PostConstruct  
    public void  init() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }  
	
	@PreDestroy
	public void dostory() {
		freeDll("opencv_java320.dll");
	}
	
	
	/**
	 * 卸载已经装载的dll
	 * 
	 * @param dllName
	 *            库名，如Decode.dll
	 **/
	@SuppressWarnings("unchecked")
	private synchronized void freeDll(String dllName) {
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			Field field = ClassLoader.class.getDeclaredField("nativeLibraries");
			field.setAccessible(true);
			Vector<Object> libs = (Vector<Object>) field.get(classLoader);
			Iterator<Object> it = libs.iterator();
			Object o;

			while (it.hasNext()) {
				o = it.next();
				Field[] fs = o.getClass().getDeclaredFields();
				boolean hasInit = false;
				for (int k = 0; k < fs.length; k++) {
					if (fs[k].getName().equals("name")) {
						fs[k].setAccessible(true);
						String dllPath = fs[k].get(o).toString();
						if (dllPath.endsWith(dllName)) {
							hasInit = true;
						}
					}
				}
				if (hasInit) {
					Method finalize = o.getClass().getDeclaredMethod("finalize", new Class[0]);
					finalize.setAccessible(true);
					finalize.invoke(o, new Object[0]);
					it.remove();
					libs.remove(o);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
