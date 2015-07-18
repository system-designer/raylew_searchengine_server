package com.searchengine.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class HtmlParser {

	public HtmlParser(){}
	
	//&quot;&nbsp;
	public String html2Text(String inputString) 
	{    	
		String htmlStr = inputString;
		String textStr ="";    
		Pattern p_script,p_style,p_html,p_filter;    
		Matcher m_script,m_style,m_html,m_filter;      
	          
	    try {
	    	String regEx_script = "<script[^>]*?>[\\s\\S]*?</script>";
	    	String regEx_style = "<style[^>]*?>[\\s\\S]*?</style>";
	    	String regEx_html = "<[^>]+>";
	        String[] filter = {"&quot;", "&nbsp;"};
	    	
	        p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);    
	        m_script = p_script.matcher(htmlStr);    
	        htmlStr = m_script.replaceAll("");
	   
	        p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);    
	        m_style = p_style.matcher(htmlStr);    
	        htmlStr = m_style.replaceAll("");
	           
	        p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);    
	        m_html = p_html.matcher(htmlStr);    
	        htmlStr = m_html.replaceAll("");

	        for(int i = 0; i < filter.length; i++)
	        {
	        	p_filter = Pattern.compile(filter[i],Pattern.CASE_INSENSITIVE);    
	        	m_filter = p_filter.matcher(htmlStr);    
		        htmlStr = m_filter.replaceAll(""); 
	        }
	        
	        textStr = htmlStr;    
	           
	    }catch(Exception e) {    
	       System.err.println("Html2Text: " + e.getMessage());    
	    }    
	          
	    return textStr;
	}

	/**
	 * 这里获取超链接太过简单，需改进
	 * @param htmlDoc
	 * @return
	 */
	public ArrayList<URL> urlDetector(String htmlDoc)
	{
		final String patternString = "<[a|A]\\s+href=([^>]*\\s*>)";   		
		Pattern pattern = Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);   
		
		ArrayList<URL> allURLs = new ArrayList<URL>();

		Matcher matcher = pattern.matcher(htmlDoc);
		String tempURL;
		while(matcher.find())
		{
			try {
				
				tempURL = matcher.group();			
				tempURL = tempURL.substring(tempURL.indexOf("\"")+1);			
				if(!tempURL.contains("\""))
					continue;
				
				tempURL = tempURL.substring(0, tempURL.indexOf("\""));
				if(tempURL.startsWith("http"))
					allURLs.add(new URL(tempURL));
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return allURLs;	
	}
	
}
