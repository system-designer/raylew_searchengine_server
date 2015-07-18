package com.searchengine.spider.beta;

import com.searchengine.domain.WebPage;
import com.searchengine.service.WebPageService;
import com.searchengine.service.impl.WebPageServiceImpl;
import com.searchengine.utils.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by RayLew on 2015/2/9.
 */
public class URLAnalyzer {
    private URLClient client = new URLClient();
    public URLAnalyzer(){}

    /**
     * 目前只分析这几种域名的网址
     */
    public String[] DOMAIN_EXTENSIONS ={".com.cn",".com",".cn",".net",".org"};

    /**
     * 得到指定url页面的所有url
     * @param url 当前网页url
     * @param webPage 当前网页实体
     * @return
     */
    public ArrayList<URL> getAllURL(URL url,WebPage webPage) {
        ArrayList<URL> urlsInHtmlDoc = null;
        try {
            Document document = client.getDocumentByURL(url, webPage);
            if (document != null) {
                webPage.setDomain(getDomainByURL(url));
                //得到该url对应的网页中的所有超链接
                Elements elements = document.getElementsByTag("a");
                if (elements != null) {
                    urlsInHtmlDoc = new ArrayList<URL>();
                    for (Element element : elements) {
                        String urlStr = element.attr("href");
                        if (StringUtils.isNotEmpty(urlStr)) {
                            urlStr = urlStr.trim();
                            URL newUrl = null;
                            //过滤掉无效的url
                            if (!Constant.INVALID_URL.contains(urlStr)) {
                                if (urlStr.startsWith("/")) {
                                    urlStr = webPage.getDomain() + urlStr;
                                }
                                if (urlStr.startsWith("http")) {
                                    newUrl = new URL(urlStr);
                                    urlsInHtmlDoc.add(newUrl);
                                }
                            }
                        }
                    }
                }
                //设置网页meta信息
                webPage.setTitle(MyHtmlParser.getTitle(document));
                webPage.setKeywords(MyHtmlParser.getKeywords(document));
                webPage.setDescription(MyHtmlParser.getDescription(document));
                //设置网页结构信息
                setPageInfoByUrl(url, webPage);
                //存储到本地磁盘
                client.saveDocument(document,webPage);
            }
        }catch(Exception ex){
            System.err.println("error:method:URLAnalyzer-getAllURL,url:"+url.toString());
            ex.printStackTrace();
        }
        return urlsInHtmlDoc;
    }

    /**
     * 通过url得到网页信息
     * @param url 当前网页url
     * @param webPage 当前网页实体
     */
    private void setPageInfoByUrl(URL url,WebPage webPage){
        String urlStr=url.toString();
        StringBuilder filePathSB=new StringBuilder();
        for(String suffix:DOMAIN_EXTENSIONS){
            if(urlStr.contains(suffix)){
                webPage.setUrl(urlStr);
                //域名分析
                String domainStr=urlStr.substring(0,urlStr.indexOf(suffix));
                if(StringUtils.isNotEmpty(domainStr)){
                    //去掉http://前缀
                    String protocol=Constant.PROTOCOL_HTTP;
                    if(StringUtils.isNotEmpty(webPage.getProtocol())){
                        protocol=webPage.getProtocol();
                    }else{
                        webPage.setProtocol(Constant.PROTOCOL_HTTP);
                    }
                    domainStr=domainStr.replaceAll(protocol+"://","");
                    String[] tempArr=domainStr.split("\\.");
                    if(tempArr!=null){
                        //域名
                        String rootDomain="";
                        for(int i=tempArr.length-1;i>=0;i--){
                            if(i==tempArr.length-1){
                                rootDomain=tempArr[i]+suffix;
                                filePathSB.append(rootDomain);
                            }else{
                                filePathSB.append("/"+tempArr[i]);
                            }
                        }
                        webPage.setRootDomain(rootDomain);
                    }
                }
                //这里的域名后链接分析有待考量，属于鸡肋
                //域名后的链接分析
                String urlSuffix=urlStr.substring(urlStr.indexOf(suffix)+suffix.length());
                if(StringUtils.isNotEmpty(urlSuffix)){
                    urlSuffix=urlSuffix.substring(1);
                    if(StringUtils.isNotEmpty(urlSuffix)&&urlSuffix.contains("/")){
                        String category=urlSuffix.substring(0,urlSuffix.indexOf("/"));
                        if(StringUtils.isNotEmpty(category)&&!category.contains("?")) {
                            filePathSB.append("/"+category);
                        }
                    }
                }
                webPage.setSavePath(filePathSB.toString());
                break;
            }
        }
    }

    /**
     * 通过url得到网页域名，即basePath
     * @param url 当前网页url
     */
    private String getDomainByURL(URL url){
        String domain="";
        String urlStr=url.toString();
        for(String suffix:DOMAIN_EXTENSIONS){
            if(urlStr.contains(suffix)){
                domain=urlStr.substring(0,urlStr.indexOf(suffix)+suffix.length());
                break;
            }
        }
        return domain;
    }

}
