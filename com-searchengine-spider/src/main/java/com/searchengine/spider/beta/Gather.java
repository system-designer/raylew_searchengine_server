package com.searchengine.spider.beta;

import com.searchengine.domain.WebPage;
import com.searchengine.service.WebPageService;
import com.searchengine.utils.StringUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * 线程类：负责采集超链接
 */
public class Gather implements Runnable {

	private int threadId;
	private Dispatcher dispatcher;
	private WebPageService webPageService;
	private URLAnalyzer urlAnalyzer=new URLAnalyzer();

	/**
	 * 构造方法：服务注入
	 * @param threadId 线程名
	 * @param dispatcher
	 * @param webPageService
	 */
	public Gather(int threadId,Dispatcher dispatcher,WebPageService webPageService) {
		this.threadId=threadId;
		this.dispatcher = dispatcher;
		this.webPageService=webPageService;
	}

	/**
	 * 线程运行实体
	 */
	@Override
	public void run() {
		int counter = 0;
		while (counter++ < 1000)
		{
			URL url = dispatcher.getURL();
			if(url!=null) {
				System.out.println("info:thread: " + threadId + ",count:"+counter+",url: "
						+ url.toString());
				//新建网页实体
				WebPage webPage=new WebPage();
				ArrayList<URL> newURL = urlAnalyzer.getAllURL(url, webPage);
				try {
					if(StringUtils.isNotEmpty(webPage.getUrl())) {
						//添加到数据库中
						webPageService.add(webPage);
					}
				}catch(Exception ex){
					System.err.println("error:method:Gather-add2DB,url:"+url.toString());
					ex.printStackTrace();
				}
				if (newURL != null && newURL.size() != 0) {
					dispatcher.insert(newURL);
				}
			}
		}
	}
}
