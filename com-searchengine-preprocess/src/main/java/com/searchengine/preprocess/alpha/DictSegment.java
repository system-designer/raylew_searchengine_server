package com.searchengine.preprocess.alpha;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import com.searchengine.utils.Configuration;

import com.searchengine.utils.HtmlParser;

public class DictSegment {

	private HashSet<String> dict; // 词表
	private HashSet<String> stopWordDict; // 停用词表
	private DictReader dictReader = new DictReader();
	private static final int maxLength = 4;
	private static String dictFile = "D:/Dictionary/wordlist.txt";
	private static String stopDictFile = "D:/Dictionary/stopWord.txt";
	private Configuration conf;

	public DictSegment() {
		conf = new Configuration();
//		dictFile = conf.getValue("DICTIONARYPATH") + "/wordlist.txt";
//		stopDictFile = conf.getValue("DICTIONARYPATH") + "/stopWord.txt";
		dict = dictReader.scanDict(dictFile);
		stopWordDict = dictReader.scanDict(stopDictFile);
	}

	/**
	 * 将网页划分为关键词语
	 * @param htmlDoc
	 * @return
	 */
	public ArrayList<String> SegmentFile(String htmlDoc) {
		HtmlParser parser = new HtmlParser();
		String htmlText = parser.html2Text(htmlDoc);
		ArrayList<String> sentence = cutIntoSentence(htmlText);
		ArrayList<String> segResult = new ArrayList<String>();
		for (int i = 0; i < sentence.size(); i++) {
			segResult.addAll(cutIntoWord(sentence.get(i)));
		}
		return segResult;
	}

	/**
	 * 将网页字符串划分为句子
	 * @param htmlDoc
	 * @return
	 */
	public ArrayList<String> cutIntoSentence(String htmlDoc) {
		ArrayList<String> sentence = new ArrayList<String>();
		String token = "。，、；：？！“”‘’《》（）-";
		StringTokenizer tokenizer = new StringTokenizer(htmlDoc, token);
		int num = tokenizer.countTokens();
		while (tokenizer.hasMoreTokens())
			sentence.add(tokenizer.nextToken());
		return sentence;
	}

	/**
	 * 将句子划分为词语
	 * @param sentence
	 * @return
	 */
	public ArrayList<String> cutIntoWord(String sentence) {
		int currLen = 0;
		String wait2cut = sentence;
		ArrayList<String> sentenceSegResult = new ArrayList<String>();
		while (wait2cut.length() != 0) {
			String temp;
			if (wait2cut.length() >= maxLength)
				currLen = maxLength;
			else
				currLen = wait2cut.length();
			temp = wait2cut.substring(0, currLen);
			while (!dict.contains(temp) && currLen > 1) {
				currLen--;
				temp = temp.substring(0, currLen);
			}
			if (!stopWordDict.contains(temp) && temp.length() != 1)
				sentenceSegResult.add(temp);
			wait2cut = wait2cut.substring(currLen);
		}
		return sentenceSegResult;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DictSegment dictSeg = new DictSegment();
	}
}
