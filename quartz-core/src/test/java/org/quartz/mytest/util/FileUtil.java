package org.quartz.mytest.util;

import java.io.FileInputStream;
import java.util.Properties;

public class FileUtil {

	
	/**
	 * 获取属性配置文件
	 * @param fileName
	 * @return
	 */
	public static Properties getProperties(String fileName){
		Properties pop=new Properties();
		
		try {
			pop.load(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pop;
	}
}
