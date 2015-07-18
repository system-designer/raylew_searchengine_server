package com.searchengine.web.controller;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by RayLew on 2014/11/8.
 * QQ:897929321
 */
public class ControllerBase {
    protected final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 请求流转成字符串
     *
     * @param request
     * @return
     * @throws java.io.IOException
     */
    protected String getRequestEntity(HttpServletRequest request) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = bis.read();
        while (read != -1) {
            baos.write(read);
            read = bis.read();
        }
        bis.close();
        byte data[] = baos.toByteArray();

        return new String(data, "utf-8");
    }
}
