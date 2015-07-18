package com.searchengine.spider.alpha;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 线程类：负责采集超链接
 */
public class Gather implements Runnable {

	private Dispatcher disp;
	private String ID;
	private URLClient client = new URLClient();
	private WebAnalyzer analyzer = new WebAnalyzer();
	private File file;
	private BufferedWriter bfWriter;

	public Gather(String ID, Dispatcher disp) {
		this.ID = ID;
		this.disp = disp;
		file = new File("D:\\Raws\\RAW__" + ID + ".txt");
		try {
			file.createNewFile();
			bfWriter = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 线程运行实体
	 */
	@Override
	public void run() {
		int counter = 0;
		while (counter++ <=50)
		{
			URL url = disp.getURL();
			System.out.println("in running: " + ID + " get url: "
					+ url.toString());
			String htmlDoc = client.getDocumentAt(url);
			if (htmlDoc.length() != 0) {
				ArrayList<URL> newURL = analyzer.doAnalyzer(bfWriter, url,
						htmlDoc);
				if (newURL.size() != 0)
					disp.insert(newURL);
			}
		}
	}
}
