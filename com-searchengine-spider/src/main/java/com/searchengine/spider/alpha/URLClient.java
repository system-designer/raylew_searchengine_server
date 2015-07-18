package com.searchengine.spider.alpha;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLClient {

	private CloseableHttpClient client;

	public URLClient()
	{

	}

	public CloseableHttpClient getCustomCloseableHttpClient() {
		return this.client == null ? HttpClients.custom().build() : client;
	}

	/**
	 * 通过url获取网页内容（字符串）
	 * @param url
	 *
	 * @return
	 */
	public String getDocumentAt(URL url)
	{
		URL hostURL = url;
		StringBuffer document = new StringBuffer();
		try {
			URLConnection conn = hostURL.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = null;
			while((line = reader.readLine()) != null)
			{
				if(!line.trim().isEmpty())
					document.append(line + "\n");
			}
				
		} catch (MalformedURLException  e) {
			System.out.println("Unable to connect to URL: " + url.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document.toString();
	}
}
