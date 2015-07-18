package com.searchengine.utils;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomUtils {
    static char c[] = {'1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o',
            'p','q',
            'r','s','t','u','v','w','x','y','z'};

    /**
     * 获取指定长度的随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) throws NoSuchAlgorithmException {

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++) {
            int a = Math.abs(random.nextInt() * 100 % c.length);
            char s = c[a];
            sb.append(s);
        }

        return sb.toString();
    }
}
