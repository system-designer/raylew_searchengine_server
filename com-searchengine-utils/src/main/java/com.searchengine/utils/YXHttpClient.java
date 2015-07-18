package com.searchengine.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class YXHttpClient {
	
	private static final Logger logger = Logger.getLogger(YXHttpClient.class);
	
	private static final int CONNECTION_TIMEOUT = 10 * 1000;
	private static final int SO_TIMEOUT = 10 * 1000;
	
	private static final String ContentType = "application/json";
	
	private YXHttpClient() {
	}

	/**
	 * HTTP GET请求
	 * @param url
	 * @param paramMap
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> paramMap) {
		String result = "";
		String params = prepareParam(paramMap);
		if (!StringUtils.isEmpty(params)) {
			if (url.indexOf("?") == -1 && url.indexOf("&") == -1) {
				url = "?" + params;
			} else {
				url = "&" + params;
			}
		}
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity httpEntity = response.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity, HTTP.UTF_8);
				}
			}
		} catch (Exception e) {
			logger.error("do get error,", e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	/**
	 * HTTP POST请求
	 * @param url
     * @param cookieValue
	 * @param paramMap
	 * @return
	 * @throws java.io.IOException
	 */
	public static String doPost(String url, String cookieValue, Map<String, Object> paramMap) throws IOException {
		String result = "";
		HttpEntity httpEntity = null;
		if (paramMap != null && !paramMap.isEmpty()) {
			JSONObject jo = new JSONObject();
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				String key = entry.getKey();
				Object value = (entry.getValue() == null ? "" : entry.getValue());
				try {
					jo.put(key, value.toString());
				} catch (JSONException e) {
					logger.error("JSON exception:", e);
				}
			}
			httpEntity = new StringEntity(jo.toString());
		}
		HttpClient httpClient = new DefaultHttpClient();
		try {
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", ContentType);
			httpPost.setHeader("Cookie", cookieValue);
			httpPost.setEntity(httpEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity, HTTP.UTF_8);
				}
			}
		} catch (Exception e) {
			logger.error("doPost error,", e);
			throw new IOException();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	/**
	 * POST JSON数据
	 * @param url
     * @param cookieValue
	 * @param json
	 * @return
	 */
	public static String doPostJSON(String url, String cookieValue, String json) {
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		try {
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", ContentType);
			httpPost.addHeader("Cookie", cookieValue);
			if (json != null) {
				StringEntity httpEntity = new StringEntity(json, "UTF-8");
				httpPost.setEntity(httpEntity);
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity, HTTP.UTF_8);
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("postjson encode error", e);
		} catch (ClientProtocolException e) {
			logger.error("postjson clientProtocolException error", e);
		} catch (IOException e) {
			logger.error("postjson ioException error", e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	/**
	 * 预处理请求参数值
	 * @param paramMap
	 * @return
	 */
	private static String prepareParam(Map<String, Object> paramMap) {
		StringBuffer buffer = new StringBuffer();
		if (paramMap == null) {
			return buffer.toString();
		}
		for (String key : paramMap.keySet()) {
			try {
				String value = (String) paramMap.get(key);
				if (buffer.length() < 1) {
					buffer.append(URLEncoder.encode(key, "UTF-8"));
					buffer.append("=");
					buffer.append(URLEncoder.encode(value, "UTF-8"));
				} else {
					buffer.append("&");
					buffer.append(URLEncoder.encode(key, "UTF-8"));
					buffer.append("=");
					buffer.append(URLEncoder.encode(value, "UTF-8"));
				}
			} catch (Exception e) {
				logger.error("encode error,", e);
			}
		}
		return buffer.toString();
	}
}
