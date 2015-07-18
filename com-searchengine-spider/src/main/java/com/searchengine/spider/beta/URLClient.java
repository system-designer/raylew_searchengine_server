package com.searchengine.spider.beta;

import com.searchengine.domain.WebPage;
import com.searchengine.utils.MD5Utils;
import com.searchengine.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.util.Date;

public class URLClient {

	// 最大连接时间：毫秒
	private static final int CONNECTION_TIMEOUT = 20 * 1000;//default:10*1000
	// socket最大连接时间：毫秒
	private static final int SO_TIMEOUT = 20 * 1000;//default:10*1000

	private CloseableHttpClient client;

	public URLClient()
	{
	}

	public CloseableHttpClient getCustomCloseableHttpClient() {
		if(this.client == null){
			this.client =HttpClients.custom().build();
		}
		return this.client;
	}

	/**
	 * 通过url得到网页内容
	 * 1.http请求时间限制
	 * 2.网页编码格式需判断
	 * @param url 当前网页url
	 * @param webPage 当前网页实体
	 * @return
	 */
	public Document getDocumentByURL(URL url,WebPage webPage){
		HttpGet get = new HttpGet(url.toString());
		CloseableHttpResponse response = null;
		Document doc=null;
		String charset=Constant.CHARSET_UTF8;
		try {
			client=getCustomCloseableHttpClient();
			response = client.execute(get);
			Header[] headers=response.getAllHeaders();
			if(headers!=null){
				for(Header header:headers){
					if(header.getName().toLowerCase().equals("content-type")){
						String contentType=header.getValue().toLowerCase();
						//如果是下载的链接则跳过
						if(contentType.contains("application/octet-stream")){
							break;
						}
						if(contentType.contains("charset")) {
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
			//暂时仅仅得到utf-8编码的网页
			doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), charset));
			String realCharset=MyHtmlParser.getCharset(doc);
			//如果response头信息中网页编码与实际文件编码不符时重新获取网页
			if(!realCharset.equals(charset)) {
				charset = realCharset;
				response = client.execute(get);
				doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), charset));
			}
			String protocol=response.getProtocolVersion().getProtocol();
			if(StringUtils.isEmpty(protocol)){
				protocol=Constant.PROTOCOL_HTTP;
			}else{
				//将形如http://的protocol过滤掉
				if(protocol!=null&&protocol.toLowerCase().contains("http")){
					protocol=Constant.PROTOCOL_HTTP;
				}
			}
			webPage.setProtocol(protocol.toLowerCase());
			response.close();
			webPage.setCharset(charset);
			//这里只将网页body部分的内容文本存入数据库
			//webPage.setContent(doc.outerHtml());
			String pageContent=doc.outerHtml();
			if (StringUtils.isNotEmpty(pageContent)){
				webPage.setContentMD5(MD5Utils.encode(pageContent));
			}
		} catch (Exception e) {
			System.err.println("error:method:URLClient-getDocumentByURL,url:"+url.toString()+",charset:"+charset);
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 将文档保存到本地
	 * @param document
	 * @param webPage
	 */
	public void saveDocument(Document document,WebPage webPage){
		Writer htmlWriter = null;
		String filePath="";
		try {
			filePath=Constant.WEBPAGE_BASEPATH+"/"+webPage.getSavePath();
			File directory=new File(filePath);
			if(!directory.exists()){
				directory.mkdirs();
			}
			Date date=new Date();
			long time=date.getTime();
			File file=new File(filePath+"/"+time+".html");
			if(!file.exists()){
				file.createNewFile();
				webPage.setSavePath(webPage.getSavePath()+"/"+time+".html");
			}
			htmlWriter = new OutputStreamWriter(new FileOutputStream(file), webPage.getCharset());
			htmlWriter.write(document.outerHtml());
			htmlWriter.close();
		} catch (Exception e) {
			System.err.println("error:method:URLClient-saveDocument,url:"+webPage.getUrl()+",filePath:"+filePath);
			e.printStackTrace();
		}
	}
}
