package com.searchengine.preprocess.alpha.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.searchengine.domain.vo.PageIndexVO;
import com.searchengine.preprocess.alpha.util.Page;
import com.searchengine.preprocess.alpha.util.DBConnection;
import com.searchengine.preprocess.alpha.util.MD5;
import com.searchengine.service.PageIndexService;

public class RawsAnalyzer {

	private DBConnection dbc = new DBConnection();
	private MD5 md5 = new MD5();
	private int offset;
	private Page page;
	private String rootDirectory;
	private PageIndexService pageIndexService;

	public RawsAnalyzer(String rootName) {
		this.rootDirectory = rootName;
		page = new Page();
	}

	public void createPageIndex(PageIndexService pageIndexService) {
		this.pageIndexService=pageIndexService;
		ArrayList<String> fileNames = getSubFile(rootDirectory);
		for (String fileName : fileNames)
			createPageIndex(fileName);
	}

	public void createPageIndex(String fileName) {
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bfReader = new BufferedReader(fileReader);
			String word;
			offset = 0;
			int oldOffset = 0;
			while ((word = bfReader.readLine()) != null) {
				oldOffset = offset;
				offset += word.length() + 1;
				String url = readRawHead(bfReader);
				String content = readRawContent(bfReader);
				String contentMD5 = md5.getMD5ofStr(content);
//				page.setPage(url, oldOffset, contentMD5, fileName);
//				page.add2DB(dbc);
				PageIndexVO pageIndex=new PageIndexVO(url,contentMD5,oldOffset,fileName);
				pageIndexService.add(pageIndex);
			}
			bfReader.close();
			System.out.println("finish the execution of this raw file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readRawHead(BufferedReader bfReader) {
		String urlLine = null;
		try {
			urlLine = bfReader.readLine();
			offset = offset + urlLine.length() + 1;
			if (urlLine != null)
				urlLine = urlLine.substring(urlLine.indexOf(":") + 1,
						urlLine.length());

			String temp;
			while (!(temp = bfReader.readLine()).trim().isEmpty()) {
				offset = offset + temp.length() + 1;
			}
			offset += 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlLine;
	}

	private String readRawContent(BufferedReader bfReader) {
		StringBuffer strBuffer = new StringBuffer();

		try {
			String word;
			while ((word = bfReader.readLine()) != null) {
				offset = offset + word.length() + 1;
				if (word.trim().isEmpty())
					break;
				else
					strBuffer.append(word + "\n");
			}
			offset += 2;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strBuffer.toString();
	}

	public static ArrayList<String> getSubFile(String fileName) {
		ArrayList<String> fileNames = new ArrayList<String>();
		File parentF = new File(fileName);
		if (!parentF.exists()) {
			System.out.println("unexisting file or directory");
			return null;
		}
		if (parentF.isFile()) {
			System.out.println("it is a file");
			fileNames.add(parentF.getAbsolutePath());
			return fileNames;
		}else if(parentF.isDirectory()) {
			String[] subFiles = parentF.list();
			for (int i = 0; i < subFiles.length; i++) {
				fileNames.add(fileName + "/" + subFiles[i]);
			}
		}
		return fileNames;
	}

}
