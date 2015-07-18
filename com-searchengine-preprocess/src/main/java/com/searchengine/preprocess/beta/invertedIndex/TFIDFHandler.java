package com.searchengine.preprocess.beta.invertedIndex;

import com.searchengine.domain.WebPage;
import com.searchengine.domain.WordDF;
import com.searchengine.service.ForwardIndexService;
import com.searchengine.service.WebPageService;
import com.searchengine.service.WordService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RayLew on 2015/2/25.
 */
public class TFIDFHandler {
    protected final Logger logger = Logger.getLogger(this.getClass());

    private ForwardIndexService forwardIndexService;
    private WordService wordService;
    private WebPageService webPageService;

    /**
     * 构造方法：service注入
     */
    public TFIDFHandler(ForwardIndexService forwardIndexService, WordService wordService, WebPageService webPageService) {
        this.forwardIndexService = forwardIndexService;
        this.wordService = wordService;
        this.webPageService = webPageService;
    }

    /**
     * 计算tf-idf
     */
    public void calculateTFIDF() {
        //从forwardindex表中得到wid-docFrequency
        List<WordDF> wordDFList = forwardIndexService.getWordDFList();
        while (wordDFList != null) {
            /*
             //修改word表中的docFrequency
            int index = 0;
            for (WordDF wordDF : wordDFList) {
                Word word = new Word();
                word.setId(wordDF.getWordId());
                word.setDocFrequency(wordDF.getDocFrequency());
                wordService.update(word);
                logger.info(index);
                index++;
            }
            */
            Map<Long, Integer> wordMap = new HashMap<Long, Integer>();
            for (WordDF wordDF : wordDFList) {
                wordMap.put(wordDF.getWordId(), wordDF.getDocFrequency());
            }
            List<WebPage> webPageList = webPageService.getList(Boolean.TRUE);
            Map<Long, Integer> webPageMap = new HashMap<Long, Integer>();
            for (WebPage webPage : webPageList) {
                webPageMap.put(webPage.getId(), webPage.getWordCount());
            }
            //修改forwardindex表中TFIDF
            int totalNum = 5017805;
            int pageSize = totalNum / (8 * 32) + 1;
            for (int times = 0; times < 8; times++) {
                for (int pageIndex = 1 + 4 * times; pageIndex <= 4 + 4 * times; pageIndex++) {
                    Thread thread = new Thread(new TFIDFThread(pageIndex, pageSize, wordMap, webPageMap, forwardIndexService));
                    thread.start();
                }
            }
        }
    }
}
