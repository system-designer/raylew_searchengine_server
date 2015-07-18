package com.searchengine.web.common;

import com.searchengine.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieUtils {

    private String LoginCookieDomain;
    private String LoginCookieName;
    private String RoleTypeCookieName;

    private Map<String, SECookie> cookieMap;

    /**
     * 从cookie中取值值，会自动解密(如果是加密保存)。
     *
     * @param servletRequest
     * @return
     */
    public String getYxCookieValue(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (cookieName.equals(LoginCookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getRoleTypeCookie(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (cookieName.equals(RoleTypeCookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取某个Cookie的值
     *
     * @param servletRequest
     * @param cookieName
     * @return
     */
    public String getCookieValue(HttpServletRequest servletRequest, String cookieName) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String ckName = cookie.getName();
                if (ckName.equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie，不管有没有定义都能删除
     *
     * @param servletResponse
     * @param name
     */
    public void deleteCookie(HttpServletResponse servletResponse, String name) {
        Cookie cookie;
        if (cookieMap != null && cookieMap.containsKey(name)) {
            SECookie seCookie = cookieMap.get(name);
            cookie = seCookie.newCookie(null);
        } else {
            cookie = new Cookie(name, null);
        }
        cookie.setMaxAge(0);
        servletResponse.addCookie(cookie);
    }

    /**
     * 设置cookie值，必须定义后才能设置。
     *
     * @param servletResponse
     * @param name
     * @param value
     */
    public void setCookie(HttpServletResponse servletResponse, String name, String Value) {
        //        if (cookieMap != null && cookieMap.containsKey(name)) {
        //            SECookie seCookie = cookieMap.get(name);
        //            Cookie cookie = seCookie.newCookie(value);
        //            servletResponse.addCookie(cookie);
        //        } else {
        //            throw new RuntimeException("Cookie " + name + " is undefined!");
        //        }

        Cookie cookie = new Cookie(name, Value);

        cookie.setDomain(CommonUtils.CookieDomain);

        cookie.setPath("/");

        cookie.setMaxAge(30 * 60);

        servletResponse.addCookie(cookie);
    }

    /**
     * 设置cookie定义值
     *
     * @param seCookieList
     */
    public void setYxCookie(List<SECookie> seCookieList) {
        if (seCookieList != null) {
            HashMap<String, SECookie> yxCookieHashMap = new HashMap<String, SECookie>(seCookieList.size());
            for (SECookie seCookie : seCookieList) {
                yxCookieHashMap.put(seCookie.getName(), seCookie);
            }
            cookieMap = yxCookieHashMap;
        }
    }

    /**
     * 删除所有状态没有设置过期的cookie
     *
     * @param request
     * @param response
     */
    public void invalidate(HttpServletRequest request, HttpServletResponse response) {
        if (cookieMap != null && cookieMap.size() > 0) {
            for (Map.Entry<String, SECookie> entry : cookieMap.entrySet()) {
                String key = entry.getKey();
                SECookie seCookie = entry.getValue();
                if (seCookie.getExpiry() < 1) {
                    if (StringUtils.isNotEmpty(getYxCookieValue(request))) {
                        deleteCookie(response, key);
                    }
                }
            }
        }
    }

    public Cookie newYxCookie(String cookieValue, int expiry) {
        Cookie cookie = new Cookie(LoginCookieName, cookieValue);
        cookie.setDomain(LoginCookieDomain);
        cookie.setPath("/");
        if (expiry > 0) {
            cookie.setMaxAge(expiry);
        }
        return cookie;
    }

    public Cookie newRoleTypeCookie(String cookieValue, int expiry) {
        Cookie cookie = new Cookie(RoleTypeCookieName, cookieValue);
        cookie.setDomain(LoginCookieDomain);
        cookie.setPath("/");
        if (expiry > 0) {
            cookie.setMaxAge(expiry);
        }
        return cookie;
    }

    public String getLoginCookieDomain() {
        return LoginCookieDomain;
    }

    public void setLoginCookieDomain(String loginCookieDomain) {
        LoginCookieDomain = loginCookieDomain;
    }

    public String getLoginCookieName() {
        return LoginCookieName;
    }

    public void setLoginCookieName(String loginCookieName) {
        LoginCookieName = loginCookieName;
    }

    public String getRoleTypeCookieName() {
        return RoleTypeCookieName;
    }

    public void setRoleTypeCookieName(String roleTypeCookieName) {
        RoleTypeCookieName = roleTypeCookieName;
    }

}
