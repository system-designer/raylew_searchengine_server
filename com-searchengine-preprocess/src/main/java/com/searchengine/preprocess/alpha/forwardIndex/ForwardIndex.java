package com.searchengine.preprocess.alpha.forwardIndex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.searchengine.preprocess.alpha.index.OriginalPageGetter;
import com.searchengine.preprocess.alpha.DictSegment;
import com.searchengine.preprocess.alpha.util.DBConnection;

public class ForwardIndex {

	private DBConnection dbc = new DBConnection();
	private HashMap<String, ArrayList<String>> indexMap = new HashMap<String, ArrayList<String>>();
	private OriginalPageGetter pageGetter = new OriginalPageGetter();
	private DictSegment dictSeg = new DictSegment();

	public ForwardIndex() {
	}

	/**
	 * 建立正排索引
	 * @return
	 */
	public HashMap<String, ArrayList<String>> createForwardIndex() {
		try {
			ArrayList<String> segResult = new ArrayList<String>();
			String sql = "select * from pageindex";
			ResultSet rs = dbc.executeQuery(sql);
			String url, fileName;
			int offset = 0;
			System.out.println("in the process of creating forwardIndex: ");
			while (rs.next()) {
				url = rs.getString("url");
				System.out.println(url);
				fileName = rs.getString("raws");
				offset = Integer.parseInt(rs.getString("offset"));
				String htmlDoc = pageGetter.getContent(fileName, offset);
				segResult = dictSeg.SegmentFile(htmlDoc);
				indexMap.put(url, segResult);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("create forwardIndex finished!!");
		System.out.println("the size of forwardIndex is : " + indexMap.size());
		return indexMap;
	}

	public static void main(String[] args) {
		ForwardIndex forwardIndex = new ForwardIndex();
		HashMap<String, ArrayList<String>> indexMap = forwardIndex
				.createForwardIndex();
		for (Iterator iter = indexMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next(); // map.entry ͬʱȡ����ֵ��
			String url = (String) entry.getKey();
			ArrayList<String> words = (ArrayList<String>) entry.getValue();
			System.out.println(url + " ��Ӧ�ķִʽ���ǣ� " + words.size());
		}
	}
}
