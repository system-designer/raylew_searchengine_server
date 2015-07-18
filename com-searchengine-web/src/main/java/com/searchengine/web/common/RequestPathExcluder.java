package com.searchengine.web.common;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class RequestPathExcluder {
	private static final Logger logger = Logger.getLogger(RequestPathExcluder.class);
	
	private String excludePath;		//需要排除的uri，如："/static,/passport"
	private String roleExcludePath;	//不需要验证用户身份的uri,如："/upload"
	
	public boolean isExclude(HttpServletRequest request) {
		return this.isExclude(request.getRequestURI());
	}
	
	public boolean isRoleExclude(HttpServletRequest request) {
        return this.isRoleExclude(request.getRequestURI());
    }
	
	public boolean isExclude(String uri) {
		//如果没有配置，获取默认的配置信息
		if(excludePath == null || excludePath.trim().length() < 1){
			logger.warn("excludePath is null.");
		}
		//如果没有配置，则认为不需要排除拦截
		if(excludePath==null || excludePath.trim().length() < 1){
			return false;
		}
		return this.isExclude(excludePath, uri);
	}
	
	public boolean isRoleExclude(String uri) {
		//如果没有配置，获取默认的配置信息
		if(roleExcludePath == null || roleExcludePath.trim().length() < 1){
			logger.warn("excludePath is null.");
		}
		//如果没有配置，则认为不需要排除拦截
		if(roleExcludePath==null || roleExcludePath.trim().length() < 1){
			return false;
		}
		return this.isRoleExclude(roleExcludePath, uri);
	}
	
	private boolean isExclude(String excludes, String uri) {
		if (excludes != null && excludes.trim().length() > 0) {
			String[] excludeArray = excludes.split(",");
			for (String exclude : excludeArray) {
				if (uri.contains(exclude)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isRoleExclude(String excludes, String uri) {
		if (excludes != null && excludes.trim().length() > 0) {
			String[] excludeArray = excludes.split(",");
			for (String exclude : excludeArray) {
				if (uri.contains(exclude)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String getExcludePath() {
		return excludePath;
	}

	public void setExcludePath(String excludePath) {
		this.excludePath = excludePath;
	}

	public String getRoleExcludePath() {
		return roleExcludePath;
	}

	public void setRoleExcludePath(String roleExcludePath) {
		this.roleExcludePath = roleExcludePath;
	}
}
