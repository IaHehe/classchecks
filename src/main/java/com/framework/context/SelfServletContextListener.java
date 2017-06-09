package com.framework.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 在Java Web项目中，我们经常会用到通过JNI调用dll动态库文件来实现一些Java不能实现的功能，
 * 或者是一些第三方dll插件。通常的做法是将这些dll文件复制到 %JAVA_HOME%\jre\bin\ 文件夹或者
 *  应用中间件（Tomcat|Weblogic）的bin目录下之后，在程序中才能正常使用。但是这个步骤在项目实施
 *  或移植时经常会被忘记，比较麻烦，所以就考虑能否在项目运行过程中动态加载，而不需要再手动复制这些文件。
 *  实现步骤：
 *  建立监听类的作用是在应用中间件启动时自动执行加载程序。
 *   1）创建一个类实现ServletContextListener 接口
 *   2）实现contextInitialized方法
 *   3）在项目的web.xml 文件中配置此监听类
 * @author DELL
 *
 */
public class SelfServletContextListener implements ServletContextListener {

	private final static Logger Log = LoggerFactory.getLogger(SelfServletContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent sContextEvent) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sContextEvent) {
		Log.info("contextInitialized.....", "");
		String dllPath = sContextEvent.getServletContext().getRealPath("WEB-INF/dll/x64/");
		addLibraryDirToPath(dllPath);
	}
	
	/**
	 * 将dll文件所在的路径添加到系统环境java.library.path 中
	 * 添加过程需要使用到反射机制来进行，不能使用System.setProperty()进行设置，设置了也无效
	 * @param path
	 */
	private boolean addLibraryDirToPath(String path) {
		
		try {
			// 获取系统path变量对象
			Field field = ClassLoader.class.getDeclaredField("sys_paths");
			// 设置此变量对象的值
			field.setAccessible(true);
			// 获取此变量的值
			String [] sysPaths = (String[]) field.get(null);
			// 添加到系统环境中的路径已经存在的时候，直接返回，不再重新加载
			for(int i = 0; i < sysPaths.length; i ++) {
				if(path.equals(sysPaths[i])) {
					return false;
				}
			}
			
			// 创建字符串数组，在原来的数组长度上增加一个，用于存放增加的目录
			String [] tmp = new String[sysPaths.length+1];
			//将原来的path变量复制到tem中
	        System.arraycopy(sysPaths,0,tmp,0,sysPaths.length);
			// 将原来的目录存入新的变量数组中
			tmp[sysPaths.length] = path;
			//System.out.println(Arrays.toString(tmp));
			// 将增加目录后的数组赋给path变量对象
			field.set(null,  tmp);
			return true;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 卸载已经装载的dll
	 * 
	 * @param dllName
	 *            库名，如Decode.dll
	 **/
	@SuppressWarnings({ "unchecked", "unused" })
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
	
	//卸载已经装载的dll
	/*private void unloadNativeLibs() {  
	    try {  
	        ClassLoader classLoader = this.getClass().getClassLoader();  
	        Field field = ClassLoader.class.getDeclaredField("nativeLibraries");  
	        field.setAccessible(true);  
	        @SuppressWarnings("rawtypes")
			Vector libs = (Vector) field.get(classLoader);  
	        @SuppressWarnings("rawtypes")
			Iterator it = libs.iterator();  
	        Object o;  
	        while (it.hasNext()) {  
	            o = it.next();  
	            Method finalize = o.getClass().getDeclaredMethod("finalize", new Class[0]);  
	            finalize.setAccessible(true);  
	            finalize.invoke(o, new Object[0]);  
	        }  
	    } catch (Exception ex) {  
	    	System.err.println("卸载dll文件出错，需要重启服务器！"+ex);
	        throw new RuntimeException(ex); 
	    }  
	} */

}









