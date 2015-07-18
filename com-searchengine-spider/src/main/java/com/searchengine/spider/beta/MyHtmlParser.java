package com.searchengine.spider.beta;

import com.searchengine.utils.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RayLew on 2015/2/9.
 */
public class MyHtmlParser {

    /**
     * 得到网页编码
     * @param document
     * @return
     */
    public static String getCharset(Document document){
        String charset="";
        Elements elements=document.getElementsByTag("meta");
        if(elements!=null&&elements.size()>0){
            for(Element element:elements){
                if(StringUtils.isNotEmpty(element.attr("charset"))){
                    charset=element.attr("charset").toLowerCase();
                    break;
                }
                if(element.attr("http-equiv").toLowerCase().equals("content-type")){
                    String contentType=element.attr("content");
                    if(StringUtils.isNotEmpty(contentType)&&contentType.contains("charset")) {
                        //后续考虑用正则改进
                        String tempStr = contentType.substring(contentType.indexOf("charset"));
                        String[] tempArr=tempStr.split("=");
                        if(tempArr!=null&&tempArr.length>1){
                            charset=tempArr[1].toLowerCase();
                        }
                    }
                    break;
                }
            }
        }
        if(charset.equals(Constant.CHARSET_GB2312)){
            charset=Constant.CHARSET_GBK;
        }
        return charset;
    }

    /**
     * 正则获取字符编码
     * @param content 网页内容
     * @return
     */
    private static String getCharset(String content){
        String regex = ".*charset=([^;]*).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if(matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    /**
     * 得到网页标题
     * @param document
     * @return
     */
    public static String getTitle(Document document){
        String title="undefined";
        Elements titleElements=document.getElementsByTag("title");
        if(titleElements!=null&&titleElements.size()>0){
            title=titleElements.get(0).text();
        }
        return title;
    }

    /**
     * 得到网页关键字
     * @param document
     * @return
     */
    public static String getKeywords(Document document){
        String keyWords="";
        Elements elements=document.getElementsByTag("meta");
        if(elements!=null&&elements.size()>0){
            for(Element element:elements){
                if(element.attr("name").toLowerCase().equals("keywords")){
                    keyWords=element.attr("content");
                    break;
                }
            }
        }
        return keyWords;
    }

    /**
     * 得到网页摘要
     * @param document
     * @return
     */
    public static String getDescription(Document document){
        String description="";
        Elements elements=document.getElementsByTag("meta");
        if(elements!=null&&elements.size()>0){
            for(Element element:elements){
                if(element.attr("name").toLowerCase().equals("description")){
                    description=element.attr("content");
                    break;
                }
            }
        }
        return description;
    }

    /**
     * HTML标签转义方法 —— java代码库
     * @param html
     * @return
     */
    public static String html2text(String html) {
        if(html==null) return "";
        html = StringUtils.replace(html, "'", "&apos;");
        html = StringUtils.replace(html, "\"", "&quot;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
        html = StringUtils.replace(html, "<", "&lt;");
        html = StringUtils.replace(html, ">", "&gt;");
        return html;
    }
}
