package com.searchengine.web.common;

import com.searchengine.security.DesSecurityComponent;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;

public class SECookie {

    /**
     * 加密工具
     */
    private DesSecurityComponent desSecurityComponent;

    /**
     * cookie的名字
     */
    private String name;

    /**
     * cookie的domain
     */
    private String domain;

    /**
     * cookie的路径
     */
    private String path;

    /**
     * cookie的过期时间 单位：秒
     */
    private int expiry;

    /**
     * cookie的key
     */
    private String key;

    /**
     * 是否加密cookie
     */
    private boolean encrypt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public Cookie newCookie(String value) {
        String newValue = value;
        if (StringUtils.isNotEmpty(value)) {
            if (isEncrypt()) {
                byte[] encryByte = desSecurityComponent.encryptBytes(value.getBytes());
                try {
                    newValue = new String(encryByte, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Cookie cookie = new Cookie(name, newValue);
        if (!StringUtils.isBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (!StringUtils.isBlank(path)) {
            cookie.setPath(path);
        }
        if (expiry > 0) {
            cookie.setMaxAge(expiry);
        }
        return cookie;
    }

    public String getValue(String value) {
        if (StringUtils.isNotEmpty(value)) {
            if (isEncrypt()) {
                byte[] desValue = desSecurityComponent.decryptBytes(value.getBytes());
                try {
                    return new String(desValue, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public DesSecurityComponent getDesSecurityComponent() {
        return desSecurityComponent;
    }

    public void setDesSecurityComponent(DesSecurityComponent desSecurityComponent) {
        this.desSecurityComponent = desSecurityComponent;
    }
}
