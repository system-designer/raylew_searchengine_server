package com.searchengine.utils;

import java.io.UnsupportedEncodingException;

public class StringUtils extends org.apache.commons.lang.StringUtils {

		public static String getStringUTF8(String string) throws UnsupportedEncodingException
		{
			return new String(string.getBytes("iso8859-1"),"utf-8");
		}
}
