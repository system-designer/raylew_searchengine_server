package com.searchengine.preprocess.alpha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.searchengine.domain.PageIndex;
import com.searchengine.domain.QueryResult;
import com.searchengine.preprocess.alpha.index.OriginalPageGetter;
import com.searchengine.utils.HtmlParser;

public class ResultGenerator {
	
	private OriginalPageGetter pageGetter;
	private HtmlParser parser;
	Pattern p_title, p_meta;    
	Matcher m_title, m_meta;
	
	public ResultGenerator()
	{
		pageGetter = new OriginalPageGetter();
		parser = new HtmlParser();

		String regEx_title = "<title[^>]*?>[\\s\\S]*?</title>"; 
		String regEx_meta = "<meta[\\s\\S]*?>";    
		p_title = Pattern.compile(regEx_title,Pattern.CASE_INSENSITIVE);
		p_meta = Pattern.compile(regEx_meta,Pattern.CASE_INSENSITIVE);
	}

	public QueryResult generateResult(String url) {
		
		PageIndex page;
		String content = "";		
		String date = "";
		String title = "";
		//String contentText = "";
		String shortContent = "";
		
		page = pageGetter.getRawsInfo(url);
		content = pageGetter.getContent(page.getRaws(), page.getOffset());

		date = pageGetter.getDate();

		m_title = p_title.matcher(content);    
		while(m_title.find())
		{
			title = m_title.group();
			title = title.substring(title.indexOf(">")+1, title.lastIndexOf("<"));
			break;
		}

		m_meta = p_meta.matcher(content);    
		while(m_meta.find())
		{
			shortContent = m_meta.group();   
			shortContent = shortContent.toLowerCase();
			
			if(shortContent.contains("description"))
			{
				int start = shortContent.indexOf("content=")+9;
			
				shortContent = (String) shortContent.
					subSequence(start, shortContent.length());
				int end = shortContent.indexOf("\"");
				shortContent =  (String) shortContent.subSequence(0, end);
				break;
			}			
		}
		
		return new QueryResult(title, shortContent, url,"","", date);
	}
	
}
