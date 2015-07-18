package com.searchengine.preprocess.alpha.invertedIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.searchengine.preprocess.alpha.forwardIndex.ForwardIndex;

public class InvertedIndex {

	private HashMap<String, ArrayList<String>> fordwardIndexMap;
	private HashMap<String, ArrayList<String>> invertedIndexMap;

	public InvertedIndex()
	{
		//建立正排索引
		ForwardIndex forwardIndex = new ForwardIndex();
		fordwardIndexMap = forwardIndex.createForwardIndex();
	}

	/**
	 * 创建倒排索引
	 * @return
	 */
	public HashMap<String, ArrayList<String>> createInvertedIndex() {
		invertedIndexMap = new HashMap<String, ArrayList<String>>();
		for (Iterator iterator = fordwardIndexMap.entrySet().iterator(); iterator.hasNext();)
		{
			Map.Entry entry = (Map.Entry) iterator.next();
			String url = (String) entry.getKey();
			ArrayList<String> words = (ArrayList<String>) entry.getValue();
			String word;
			for(int i = 0; i < words.size(); i++)
			{
				word = words.get(i);
				if(!invertedIndexMap.containsKey(word))
				{
					ArrayList<String> urls = new ArrayList<String>();
					urls.add(url);
					invertedIndexMap.put(word, urls);
				}
				else
				{
					ArrayList<String> urls = invertedIndexMap.get(word);
					if(!urls.contains(url))
						urls.add(url);
				}
			}
		}

		System.out.println("***************************************************************");
		System.out.println("create invertedIndex finished!!");
		System.out.println("the size of invertedIndex is : " + invertedIndexMap.size());
		return invertedIndexMap;
	}

	public HashMap<String, ArrayList<String>> getInvertedIndex()
	{
		return invertedIndexMap;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		InvertedIndex invertedIndex = new InvertedIndex();
		HashMap<String, ArrayList<String>> invertedIndexMap = invertedIndex.createInvertedIndex();
		
		String key = "��";
		ArrayList<String> urls = invertedIndexMap.get(key);
		
		if(urls != null)
		{
			System.out.println("�õ��˽�����£�");
			for(String url : urls)
				System.out.println(url);
		}
		else
		{
			System.out.println("���ϧ��û�ҵ���Ҫ�����Ĺؼ���");
		}
	}

}
