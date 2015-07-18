package com.searchengine.utils;

import java.util.List;
import java.util.ArrayList;

public class CommonUtils {
	
	public static final int ERROR = -1;
	public static final int SUCCESS = 0;
	
	public static final Long SYS_NOTIFY_USER_ID = 10000001234567L;
	
	public static final String CookieDomain = "localhost";
	public static final String CookieName = "YXSSID";

	public static final String MediaTypeJSON = "application/json; charset=utf-8";
	
	public static final int MinPasswordLength = 6;
	
	public static final String Sort_Desc="desc";
	public static final String Sort_ASC ="asc";

	/**
	 * 已删除标识
	 */
	public static final int Flag_DELETED=-1;
	/**
	 * 数据未删除标识(数据有效)
	 */
	public static final int Flag_NOTDELETED=1;
	/**
	 * 与老平台进行加密、解密使用的key
	 */
	public static final String AES_KEY_IDSP_STRING="mobile987DEF@joyschool.cn";
	/**
	 * 与老平台进行加密、解密使用的偏移量
	 */
    @SuppressWarnings("unused")
    public static final byte[] AES_VECTOR_IDSP_BYTE = { 0x41, 0x72, 0x65, 0x79, 0x6F, 0x75, 0x6E,
        0x79, 0x53, 0x6E, 0x6F, 0x77, 0x6D, 0x61, 0x6E, 0x3F}; 
    /**
     * 获取用户状态缓存KEY
     * @param userId
     * @return
     */
	public static final String getPassportKey(Long userId) {
		return "Yunxiao.PassportService.UserToken.UserId." + userId;
	}
	
	public static final String getPassportVerifyCode(String activationCode) {
		return "Passport.Activation.ActivationCode." + activationCode;
	}
	
	/**
	 * 切分字符串
	 * @param input and split,  input = "a,b,c,dd"  split = "," return is " a b c dd"
	 * @hxk
	 */
	public static final List<String> split_str(String input, String split) {
		List<String> result = new ArrayList<String>();
		int index = input.indexOf(split);
		int beg = 0;
		while(index != -1) {
			String str = input.substring(beg, index);
			beg = index + 1;
			result.add(str);
			index = input.indexOf(split, beg);
		}
		if(index == -1) {
			String str = input.substring(beg);
			result.add(str);
		}
		return result;
	}
}
