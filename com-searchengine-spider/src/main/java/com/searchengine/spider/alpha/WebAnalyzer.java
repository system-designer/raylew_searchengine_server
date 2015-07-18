package com.searchengine.spider.alpha;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import com.searchengine.utils.HtmlParser;

public class WebAnalyzer {

	/**
	 * 页面终止符
	 */
	private static final String ENDPAGE = "**************************************************";

	public WebAnalyzer() {
	}

	public ArrayList<URL> doAnalyzer(BufferedWriter bfWriter, URL url,
			String htmlDoc) {
		System.out.println("in doing analyzer the size of doc is: "
				+ htmlDoc.length());
		//得到该url对应的网页中的所有超链接
		ArrayList<URL> urlInHtmlDoc = (new HtmlParser()).urlDetector(htmlDoc);
		//存入网页库
		saveDoc(bfWriter, url, htmlDoc);
		return urlInHtmlDoc;
	}

	// example:
	// version:1.0
	// url:http//www.pku.edu.cn
	// origin:http://www.somewhere.cn
	// date:Tue, 15 Apr 2003 08:13:06 GMT
	// ip:162.105.129.12
	// length:18133
	//
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// data segement
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	//
	/**
	 * 网页内容暂时存在txt中，在txt中标记该网页的起始位置
	 * @param bfWriter
	 * @param url
	 * @param htmlDoc
	 */
	private void saveDoc(BufferedWriter bfWriter, URL url, String htmlDoc) {
		try {
			String versionStr = "version:1.0\n";
			String URLStr = "url:" + url.toString() + "\n";
			Date date = new Date();
			String dateStr = "date:" + date.toString() + "\n";
			InetAddress address = InetAddress.getByName(url.getHost());
			String IPStr = address.toString();
			IPStr = "IP:"
					+ IPStr.substring(IPStr.indexOf("/") + 1, IPStr.length())
					+ "\n";
			String htmlLen = "length:" + htmlDoc.length() + "\n";
			bfWriter.append(versionStr);
			bfWriter.append(URLStr);
			bfWriter.append(dateStr);
			bfWriter.append(IPStr);
			bfWriter.append(htmlLen);
			bfWriter.newLine();
			bfWriter.append(htmlDoc);
			// bfWriter.append(ENDPAGE);
			// bfWriter.newLine();
			bfWriter.newLine();
			bfWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
