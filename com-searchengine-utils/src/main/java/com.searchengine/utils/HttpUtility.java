package com.searchengine.utils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
 

public class HttpUtility {
    public static final String TAG_HTTP = "HttpUtility";
    public String SVR_URL;
    private String _httpUrl;
    public String GetHttpUrl() {
        return _httpUrl;
    }
    private List<NameValuePair> _params;
    public List<NameValuePair> GetHttpParams() {
        return _params;
    }
    public void AddParam(NameValuePair param) {
        _params.add(param);
    }
    private List<NameValuePair> _headers;
    public List<NameValuePair> GetHttpHeaders() {
        return _headers;
    }
    public void AddHeader(NameValuePair header) {
        _headers.add(header);
    }
    public HttpUtility(String httpUrl) {
        _httpUrl = httpUrl;
        _params = new ArrayList<NameValuePair>();
        _headers=new ArrayList<NameValuePair>();
    }
    public HttpUtility(String httpUrl, List<NameValuePair> httpParams) {
        _httpUrl = httpUrl;
        if (httpParams == null) {
            _params = new ArrayList<NameValuePair>();
        } else {
            _params = httpParams;
        }
        _headers=new ArrayList<NameValuePair>();
    }
 
 
    public String GetResponseFromHttpPost() {
    //    DebugLog.logd(TAG_HTTP, "GetResponseFromHttpPost--_httpUrl = " + _httpUrl);
        String strResult = "";
        try {
            HttpPost httpRequest = new HttpPost(_httpUrl);
            // httpRequest.addHeader(params.get(0).getName(),
            if (_headers != null && _headers.size() > 0) {
                // 
                for (int i = 0; i < _headers.size(); i++) {
                    httpRequest.addHeader(_headers.get(i).getName(),_headers.get(i).getValue());
                }
            }
            // 
            if (_params != null && _params.size() > 0) {
                // 
                HttpEntity httpentity = new UrlEncodedFormEntity(_params,
                        HTTP.UTF_8);
                // 
                httpRequest.setEntity(httpentity);
            }
            
            //
            HttpClient httpclient = new DefaultHttpClient();
            //
            HttpParams httpParams = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 40000);
            HttpConnectionParams.setSoTimeout(httpParams, 40000);
            
       //     DebugLog.logd(TAG_HTTP, "before httpclient.execute(httpRequest)");
            HttpResponse httpResponse = httpclient.execute(httpRequest);
         //   DebugLog.logd(TAG_HTTP, "httpclient.execute(httpRequest)");
            
            // 
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 
                strResult = EntityUtils.toString(httpResponse.getEntity());
             //   Log.i(TAG_HTTP, "httpResponse 200");
//                Log.i(TAG_HTTP, "strResult = " + strResult);
                
            } else {
              //  Log.i(TAG_HTTP, "httpResponse : "
                //        + httpResponse.getStatusLine().getStatusCode());
              //  Log.i(TAG_HTTP, "EntityUtils = " + EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
          //  Log.e(TAG_HTTP, "GetResponseFromHttpPost exception");
        }
     //   Log.i(TAG_HTTP, "strResult="+strResult);

        return strResult;
    }
  

    public String GetResponseFromHttpGet() {
        String strResult = "";
        try {
            StringBuilder urlBuilder=new StringBuilder(_httpUrl);
            if (_params != null && _params.size() > 0) {
                urlBuilder.append("?");
                for (int i = 0; i < _params.size(); i++) {
                    urlBuilder
                    .append(URLEncoder.encode(_params.get(i).getName(), "UTF-8"))
                    .append('=')
                    .append(URLEncoder.encode(_params.get(i).getValue(), "UTF-8"));
                    if(i<_params.size()-1)
                    {
                        urlBuilder.append("&");
                    }
                }
            }
            // HttpGet
            HttpGet httpRequest = new HttpGet(urlBuilder.toString());
            // httpRequest.addHeader(params.get(0).getName(),
            if (_headers != null && _headers.size() > 0) {
                // HttpHeader
                for (int i = 0; i < _headers.size(); i++) {
                    httpRequest.addHeader(_headers.get(i).getName(),_headers.get(i).getValue());
                }
            }
            //
            HttpClient httpclient = new DefaultHttpClient();
            //
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            // 
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //
                strResult = EntityUtils.toString(httpResponse.getEntity());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }

    public String GetResponseFromHttpUrlGet(String httpUrl) {
        String resultData = "";
        try {
            URL url = new URL(httpUrl);
            //
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            // 
            InputStreamReader in = new InputStreamReader(
                    urlConn.getInputStream());
            // 
            BufferedReader buffer = new BufferedReader(in);
            String inputLine = null;
            //
            while (((inputLine = buffer.readLine()) != null)) {
                // 
                resultData += inputLine + "\n";
            }
            // 
            in.close();
            // 
            urlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }




    /**
     * 发送https请求
     * @param url
     * @param paramsMap
     * @param method：GET OR POST
     * @return: response result
     */
    public static String doHttpsRequest(String url, Map<String, String> paramsMap, String method)
    {
        if(null == url || url.length() <= 0)
        {
            throw  new IllegalArgumentException("url");
        }

        if(null == method || method.length() <= 0)
            method = "GET";
        String result = null;
        org.apache.commons.httpclient.HttpClient http = new org.apache.commons.httpclient.HttpClient();
        http.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

        Protocol myhttps = new Protocol("https", new CustomSSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);

        PostMethod post = null;
        GetMethod get = null;
        if(method == "GET"){
            if (paramsMap != null && paramsMap.size() > 0) {
                ArrayList list = new ArrayList();
                for (String key : paramsMap.keySet()) {
                    list.add(key + "=" + paramsMap.get(key));
                }
                String parms = org.apache.commons.lang.StringUtils.join(list, "&");
                url += "?" + parms;
            }
            get = new GetMethod(url);
        } else {
            post = new PostMethod(url);
            if (paramsMap != null && paramsMap.size() > 0) {
                org.apache.commons.httpclient.NameValuePair[] datas = new org.apache.commons.httpclient.NameValuePair[paramsMap.size()];
                int index = 0;
                for (String key : paramsMap.keySet()) {
                    datas[index++] = new org.apache.commons.httpclient.NameValuePair(key, paramsMap.get(key));
                }
                post.setRequestBody(datas);
            }
            HttpClientParams httparams = new HttpClientParams();
            httparams.setSoTimeout(60000);
            post.setParams(httparams);
        }

        try {
            int statusCode = (post != null) ? http.executeMethod(post) : http.executeMethod(get);
            if (statusCode == org.apache.commons.httpclient.HttpStatus.SC_OK) {
                result = (post != null) ?  post.getResponseBodyAsString() : get.getResponseBodyAsString();
            } else {
                System.out.println(" http response status is " + statusCode);
            }

        } catch (HttpException e) {
            System.out.println("error url=" + url + ":" + e);
        } catch (IOException e) {
            System.out.println("error url=" + url + ":" + e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return  result;
    }

    /**
     *
     * @param url
     * @param paramsMap
     * @param method : GET or POST
     * @return
     */
    public static String doHttpRequest(String url, Map<String, String> paramsMap, String method)
    {
        String result = null;
        URL u = null;
        HttpURLConnection con = null;

        if(null == method || method.length() <= 0)
            method = "GET";

        //尝试发送请求
        try {
            //构建请求参数
            StringBuffer sb = new StringBuffer();
            int i = 1, size = paramsMap.size();
            for(String key : paramsMap.keySet())
            {
                sb.append(key + "=" + URLEncoder.encode(paramsMap.get(key)));
                if(i < size)
                {
                    sb.append("&");
                }
            }
            if(method == "GET")
            {
                url += "?" + sb.toString();
                u = new URL(url);
            }
            if(method == "POST")
            {
                u = new URL(url);
                con = (HttpURLConnection)u.openConnection();
                con.setRequestMethod(method);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
                osw.write(sb.toString());
                osw.flush();
                osw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        //读取返回内容
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            result = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

}
