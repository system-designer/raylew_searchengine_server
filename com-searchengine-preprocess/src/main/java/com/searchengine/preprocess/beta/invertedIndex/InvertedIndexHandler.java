package com.searchengine.preprocess.beta.invertedIndex;

import com.searchengine.domain.Word;
import com.searchengine.service.InvertedIndexService;
import com.searchengine.service.RedisService;
import com.searchengine.service.WebPageService;
import com.searchengine.service.WordService;

import java.util.List;

public class InvertedIndexHandler {
    private WordService wordService;
    private int threadNum = 32;
    private InvertedIndexService invertedIndexService;
    private RedisService redisService;
    private WebPageService webPageService;

    /**
     * 构造方法：服务注入
     *
     * @param wordService
     * @param invertedIndexService
     */
    public InvertedIndexHandler(WordService wordService, InvertedIndexService invertedIndexService, RedisService redisService) {
        this.wordService = wordService;
        this.invertedIndexService = invertedIndexService;
        this.redisService = redisService;
    }

    /**
     * 建立倒排索引
     *
     * @return
     */
    public void createInvertedIndex() {
        //得到词表
        List<Word> wordList = null;
        Object obj = redisService.get("wordList");
        if (obj != null) {
            wordList = (List<Word>) obj;
            if (wordList.size() > 130000) {
                redisService.remove("wordList");
                wordList = wordService.getList();
                redisService.add("wordList", wordList);
            }
        } else {
            wordList = wordService.getList();
            redisService.add("wordList", wordList);
        }
        WordDispatcher wordDispatcher = new WordDispatcher(wordList);
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(new InvertedIndexThread(i, invertedIndexService, redisService, wordDispatcher));
            thread.start();
        }
    }
}
