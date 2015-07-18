package com.searchengine.spider.alpha;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Spider {

	/**
	 * 待处理的url列表
	 */
	private ArrayList<URL> urls;
	/**
	 * 爬虫线程个数
	 */
	private int gatherNum = 10;

	public Spider() {
	}

	public Spider(ArrayList<URL> urls) {
		this.urls = urls;
	}

	/**
	 * 开始执行线程
	 */
	public void start() {
		Dispatcher dispatcher = new Dispatcher(urls);
		for (int i = 0; i < gatherNum; i++) {
			Thread gather = new Thread(new Gather(String.valueOf(i), dispatcher));
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
