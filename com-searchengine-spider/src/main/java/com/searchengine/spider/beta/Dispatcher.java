package com.searchengine.spider.beta;

import java.net.URL;
import java.util.ArrayList;

/**
 * 负责分发url到不同的线程
 */
public class Dispatcher {

	/**
	 * 当前还需访问的url
	 */
	private static ArrayList<URL> urls = new ArrayList<URL>();
	/**
	 * 已访问的url
	 */
	private static ArrayList<URL> visitedURLs = new ArrayList<URL>();
	
	public Dispatcher(ArrayList<URL> urls) {    
		this.urls = urls; 
	}

	public Dispatcher(ArrayList<URL> urls,ArrayList<URL> visitedURLs) {
		this.urls = urls;
		this.visitedURLs=visitedURLs;
	}

	/**
	 * 同步从url列表中得到url
	 * @return
	 */
	public synchronized URL getURL()		
	{
		while(urls.isEmpty()){ 
			try{ 
				wait();
			} catch (InterruptedException e) { 
				e.printStackTrace(); 
			} 
		}		
		this.notify(); 
		URL url = urls.get(0);
		visitedURLs.add(url);
		urls.remove(url);
		
	    return url; 
	}

	/**
	 * 将新的url插入到url列表
	 * @param url
	 */
	public synchronized void insert(URL url)
	{
		if(!urls.contains(url) && !visitedURLs.contains(url))
			urls.add(url);
	}

	/**
	 * 批量插入
	 * @param analyzedURL
	 */
	public synchronized void insert(ArrayList<URL> analyzedURL)
	{
		for(URL url : analyzedURL)
		{
			if(!urls.contains(url) && !visitedURLs.contains(url))
			urls.add(url);
		}
	}
    
}
