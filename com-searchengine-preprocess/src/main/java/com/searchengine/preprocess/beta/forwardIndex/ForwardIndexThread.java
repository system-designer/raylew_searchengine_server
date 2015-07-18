package com.searchengine.preprocess.beta.forwardIndex;

import com.searchengine.domain.ForwardIndex;
import com.searchengine.domain.WebPage;
import com.searchengine.domain.Word;
import com.searchengine.service.ForwardIndexService;
import com.searchengine.service.WebPageService;
import com.searchengine.utils.Constant;
import com.searchengine.utils.FileAction;
import com.searchengine.utils.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ForwardIndexThread implements Runnable {
    protected final Logger logger = Logger.getLogger(this.getClass());

    private ForwardIndexService forwardIndexService;
    private WebPageService webPageService;
    private WebPageDispatcher webPageDispatcher;
    private List<Word> wordList;
    private List<WebPage> webPageList;
    private int threadId;

    /**
     * 构造方法：服务注入
     *
     * @param threadId
     * @param forwardIndexService
     * @param webPageService
     * @param wordList
     * @param webPageList
     */
    public ForwardIndexThread(int threadId, ForwardIndexService forwardIndexService, WebPageService webPageService, List<Word> wordList, List<WebPage> webPageList) {
        this.threadId = threadId;
        this.forwardIndexService = forwardIndexService;
        this.webPageService = webPageService;
        this.wordList = wordList;
        this.webPageList = webPageList;
        webPageDispatcher = new WebPageDispatcher(webPageList);
    }

    @Override
    public void run() {
        int size = Integer.MAX_VALUE;
        while (size > 0) {
            size = webPageList.size();
            logger.info(size);
            try {
                WebPage webPage = webPageDispatcher.getWebPage();
                String charset = Constant.CHARSET_UTF8;
                if (StringUtils.isNotEmpty(webPage.getCharset())) {
                    charset = webPage.getCharset();
                }
                String text = FileAction.convertFileToString(Constant.WEBPAGE_BASEPATH + "/" + webPage.getSavePath(), charset);
                Document document = Jsoup.parse(text);
                text = document.text();
                StringReader sr = new StringReader(text);
                IKSegmenter ik = new IKSegmenter(sr, true);
                Lexeme lex = null;
                //记录用到的word，利用程序的局部性原理
                HashMap<String, Long> wordMap = new HashMap<String, Long>();
                HashMap<Long, Integer> matchedTimesMap = new HashMap<Long, Integer>();
                HashMap<Long, Integer> matchedPosition = new HashMap<Long, Integer>();
                int wordCount = 0;
                while ((lex = ik.next()) != null) {
                    wordCount++;
                    String matchedWord = lex.getLexemeText();
                    //这里应该去除停用词
                    Long key = null;
                    if (wordMap.containsKey(matchedWord)) {
                        key = wordMap.get(matchedWord);
                    } else {
                        for (Word word : wordList) {
                            if (word.getName().equals(matchedWord)) {
                                key = word.getId();
                                wordMap.put(matchedWord, key);
                                break;
                            }
                        }
                    }
                    if (key != null) {
                        if (matchedTimesMap.containsKey(key)) {
                            matchedTimesMap.put(key, matchedTimesMap.get(key) + 1);
                        } else {
                            matchedTimesMap.put(key, 1);
                            matchedPosition.put(key, lex.getBeginPosition());
                        }
                    }
                }
                if (matchedTimesMap != null && matchedPosition != null) {
                    for (Map.Entry<Long, Integer> entry : matchedTimesMap.entrySet()) {
                        ForwardIndex forwardIndex = new ForwardIndex();
                        forwardIndex.setPid(webPage.getId());
                        forwardIndex.setWid(entry.getKey());
                        forwardIndex.setMatchTimes(entry.getValue());
                        forwardIndex.setPosition(matchedPosition.get(entry.getKey()));
                        forwardIndexService.add(forwardIndex);
                    }
                }
                //同时设置网页的词数
                webPage.setWordCount(wordCount);
                webPageService.update(webPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
