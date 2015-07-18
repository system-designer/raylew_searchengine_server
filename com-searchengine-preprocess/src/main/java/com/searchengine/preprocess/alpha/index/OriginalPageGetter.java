package com.searchengine.preprocess.alpha.index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.searchengine.preprocess.alpha.util.DBConnection;
import com.searchengine.preprocess.alpha.util.MD5;
import com.searchengine.utils.Configuration;

import com.searchengine.domain.PageIndex;

public class OriginalPageGetter {

	private String url = "";
	private DBConnection dbc = new DBConnection();
	private MD5 md5 = new MD5();
	private String date = "";
	private String urlFromHead = "";
	private Configuration conf = new Configuration();

	public OriginalPageGetter() {
	}

	public OriginalPageGetter(String url) {
		this.url = url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public String getPage() {
		PageIndex page = getRawsInfo(url);
		String content = "";
		try {
			StringBuffer tfileName = new StringBuffer();
//			tfileName.append(page.getRaws());
//			tfileName.insert(4, "\\");
//			String fileName = conf.getValue("RAWSPATH") + "\\"
//					+ tfileName.toString();
			String fileName=page.getRaws();

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bfReader = new BufferedReader(fileReader);

			String word;
			bfReader.skip(page.getOffset());

			readRawHead(bfReader);
			content = readRawContent(bfReader);
			String contentMD5 = md5.getMD5ofStr(content);

			if (contentMD5.equals(page.getContent()))
				System.out.println("一样的哦");
			else
				System.out.println("不一样的哦");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	/**
	 * 从本地网页库得到网页内容
	 * @param file
	 * @param offset
	 * @return
	 */
	public String getContent(String file, int offset) {
		String content = "";
		BufferedReader bfReader = null;
		try {
//			String fileName = conf.getValue("RAWSPATH") + "/"+ file.replaceAll("Raws", "");
			String fileName = file;
			FileReader fileReader = new FileReader(fileName);
			bfReader = new BufferedReader(fileReader);
			String word;
			bfReader.skip(offset);
			readRawHead(bfReader);
			content = readRawContent(bfReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bfReader != null)
				try {
					bfReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return content;
	}

	/**
	 * 获取网页库中存储的页面所有信息
	 * @param url
	 * @return
	 */
	public PageIndex getRawsInfo(String url) {
		String sql = " select * from pageindex where url='" + url + "' ";
		ResultSet rs = dbc.executeQuery(sql);
		String content = "";
		String raws = "";
		int offset = 0;
		try {
			while (rs.next()) {
				content = rs.getString("content");
				offset = Integer.parseInt(rs.getString("offset"));
				raws = rs.getString("raws");
				System.out.println(url + "\t" + content + "\t" + offset + "\t"
						+ raws);
			}
			return new PageIndex(url, content,offset, raws);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取网页库中存储的页面头部
	 * @param bfReader
	 * @return
	 */
	private String readRawHead(BufferedReader bfReader) {
		// String urlLine = null;
		String headStr = "";
		try {
			bfReader.readLine(); // version
			urlFromHead = bfReader.readLine();
			headStr += urlFromHead;
			if (urlFromHead != null)
				urlFromHead = urlFromHead.substring(
						urlFromHead.indexOf(":") + 1, urlFromHead.length());
			date = bfReader.readLine();
			headStr += date;
			if (date != null)
				date = date.substring(date.indexOf(":") + 1, date.length());
			String temp;
			while (!(temp = bfReader.readLine()).trim().isEmpty()) {
				headStr += temp;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return headStr;
	}

	/**
	 * 获取网页库中存储的页面主要内容
	 * @param bfReader
	 * @return
	 */
	private String readRawContent(BufferedReader bfReader) {
		StringBuffer strBuffer = new StringBuffer();
		try {
			String word;
			while ((word = bfReader.readLine()) != null) {
				if (word.trim().isEmpty())
					break;
				else
					strBuffer.append(word + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strBuffer.toString();
	}
}
