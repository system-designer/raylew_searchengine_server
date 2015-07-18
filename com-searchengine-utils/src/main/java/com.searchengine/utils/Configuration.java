package com.searchengine.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

	private Properties propertie;
	private FileInputStream inputFile;
	private FileOutputStream outputFile;

	public Configuration() {
		propertie = new Properties();
		try {
			propertie.load(getClass().getClassLoader().
					getResourceAsStream("configure.properties"));
		} catch (FileNotFoundException ex) {
			System.out.println("文件路径错误或者文件不存在");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("装载文件失败!");
			ex.printStackTrace();
		}
	}

	public String getValue(String key) {
		if (propertie.containsKey(key)) {
			String value = propertie.getProperty(key);
			return value;
		} else
			return "";
	}


	public static void main(String[] args) {
		Configuration conf = new Configuration();

		String rawsPath = conf.getValue("RAWSPATH");
		String dictPath = conf.getValue("DICTIONARYPATH");
		String mysqlPath = conf.getValue("MYSQLLIBPATH");

		System.out.println("the rawsPath is " + rawsPath);
		System.out.println("the dictPath is " + dictPath);
		System.out.println("the mysqlPath is " + mysqlPath);
	}

}
