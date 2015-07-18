package com.searchengine.spider.beta;

import com.searchengine.service.WebPageService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 爬虫的入口程序
 */
public class Spider {

	/**
	 * 待处理的url列表
	 */
	private ArrayList<URL> urls;

	/**
	 * 已处理的url列表
	 */
	private ArrayList<URL> visitedUrls;
	/**
	 * 爬虫线程个数
	 */
	private int gatherNum = 16;

	/**
	 * 网页服务
	 */
	private WebPageService webPageService;

	public Spider() {

	}

	public Spider(ArrayList<URL> urls) {
		this.urls = urls;
	}

	/**
	 * 构造方法：服务注入
	 * @param urls
	 * @param visitedUrls
	 * @param webPageService
	 */
	public Spider(ArrayList<URL> urls,ArrayList<URL> visitedUrls,WebPageService webPageService) {
		this.urls = urls;
		this.visitedUrls=visitedUrls;
		this.webPageService=webPageService;
	}

	/**
	 * 开始执行线程
	 */
	public void start() {
		Dispatcher dispatcher = new Dispatcher(urls,visitedUrls);
		for (int i = 0; i < gatherNum; i++) {
			Thread gather = new Thread(new Gather(i, dispatcher,this.webPageService));
			gather.start();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<URL> urls = new ArrayList<URL>();
		try {
			urls.add(new URL("http://www.hao123.com"));
			urls.add(new URL("http://www.sina.com"));
			urls.add(new URL("http://www.sohu.com/"));
			urls.add(new URL("http://www.china.com/"));
			urls.add(new URL("http://www.163.com/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Spider spider = new Spider(urls);
		spider.start();
	}

}
