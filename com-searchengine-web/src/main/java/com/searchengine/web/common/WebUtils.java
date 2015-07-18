package com.searchengine.web.common;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * Created by [Eay Lew] on 2014/11/17.
 */
public class WebUtils {
    private static final Logger logger = Logger.getLogger(WebUtils.class);

    public static String getRequestHostAndPort(HttpServletRequest request) {
        try {
            URL requestURL = new URL(request.getRequestURL().toString());
            String domain = requestURL.getHost();
            int port = requestURL.getPort();
            return domain + ":" + port;
        } catch (Exception e) {
            logger.error("get request host error:", e);
        }
        return null;
    }

    public static String getImageViewUrl(HttpServletRequest request) {
        return getRequestHostAndPort(request) + "/image/view/";
    }

    public static String getImageViewUrl(HttpServletRequest request, String url) {
        return getRequestHostAndPort(request) + "/image/view/" + url;
    }

    public static String getAppDownloadUrl(HttpServletRequest request, String attachmentId) {
        return getRequestHostAndPort(request) + "/download/app/" + attachmentId;
    }
}
