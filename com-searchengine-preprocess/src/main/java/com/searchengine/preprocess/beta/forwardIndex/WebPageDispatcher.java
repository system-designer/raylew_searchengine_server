package com.searchengine.preprocess.beta.forwardIndex;

import com.searchengine.domain.WebPage;

import java.util.List;

/**
 * 负责分发网页到不同的线程
 */
public class WebPageDispatcher {

	/**
	 * 当前还需访问的url
	 */
	private List<WebPage> webPageList = null;

	public WebPageDispatcher(List<WebPage> webPageList){
		this.webPageList=webPageList;
	}

	/**
	 * 同步从url列表中得到url
	 * @return
	 */
	public synchronized WebPage getWebPage()
	{
		while(webPageList.isEmpty()){
			try{ 
				wait();
			} catch (InterruptedException e) { 
				e.printStackTrace(); 
			} 
		}		
		this.notify();
		WebPage webPage = webPageList.get(0);
		webPageList.remove(webPage);
		
	    return webPage;
	}
    
}
