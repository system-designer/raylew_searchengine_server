package com.searchengine.preprocess.beta.invertedIndex;

import com.searchengine.domain.InvertedIndex;
import com.searchengine.domain.Word;
import com.searchengine.service.InvertedIndexService;
import com.searchengine.service.RedisService;
import org.apache.log4j.Logger;

import java.util.List;


public class InvertedIndexThread implements Runnable {
    protected final Logger logger = Logger.getLogger(this.getClass());

    private InvertedIndexService invertedIndexService;
    private RedisService redisService;
    private WordDispatcher wordDispatcher;
    private int threadId;

    /**
     * 构造方法：服务注入
     *
     * @param threadId
     * @param invertedIndexService
     * @param redisService
     * @param wordDispatcher
     */
    public InvertedIndexThread(int threadId, InvertedIndexService invertedIndexService, RedisService redisService, WordDispatcher wordDispatcher) {
        this.threadId = threadId;
        this.invertedIndexService = invertedIndexService;
        this.redisService = redisService;
        this.wordDispatcher = wordDispatcher;
    }

    @Override
    public void run() {
        int size = Integer.MAX_VALUE;
        while (size > 0) {
            size = wordDispatcher.getSize();
            logger.info(size);
            Word word = wordDispatcher.getWord();
            Object obj = redisService.get("index" + word.getId());
            if (obj == null) {
                List<InvertedIndex> invertedIndexList = invertedIndexService.getList(word.getId());
                redisService.add("index" + word.getId(), invertedIndexList);
            }
        }
    }
}
