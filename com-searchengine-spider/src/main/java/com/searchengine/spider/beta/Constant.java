package com.searchengine.spider.beta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RayLew on 2015/2/11.
 */
public class Constant {
    public final static String WEBPAGE_BASEPATH="F:/searchengine/raws";
    public final static List<String> INVALID_URL= (List<String>) Arrays.asList(new String[]{"#","javascript","javascript:;","javascript:void(0);","/"});

    public final static String PROTOCOL_HTTP="http";
    public final static String PROTOCOL_HTTPS="https";

    public final static String CHARSET_UTF8="utf-8";
    public final static String CHARSET_GBK="gbk";
    public final static String CHARSET_GB2312="gb2312";
}
