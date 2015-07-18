package com.searchengine.preprocess.beta.invertedIndex;

import com.searchengine.domain.ForwardIndex;
import com.searchengine.service.ForwardIndexService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by RayLew on 2015/5/11.
 * QQ:897929321
 */
public class TFIDFThread implements Runnable {
    protected final Logger logger = Logger.getLogger(this.getClass());
    private int pageIndex;
    private int pageSize;
    private Map<Long, Integer> wordMap;
    private Map<Long, Integer> pageMap;
    private ForwardIndexService forwardIndexService;

    public TFIDFThread(int pageIndex, int pageSize, Map<Long, Integer> wordMap, Map<Long, Integer> pageMap, ForwardIndexService forwardIndexService) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.wordMap = wordMap;
        this.pageMap = pageMap;
        this.forwardIndexService = forwardIndexService;
    }

    @Override
    public void run() {
        List<ForwardIndex> forwardIndexList = forwardIndexService.getList(pageIndex, pageSize);
        if (forwardIndexList != null) {
            logger.info("page No." + pageIndex + " started!");
            for (ForwardIndex forwardIndex : forwardIndexList) {
                int docFrequency = 0;
                Object obj1 = wordMap.get(forwardIndex.getWid());
                if (obj1 != null) {
                    docFrequency = (Integer) obj1;
                }
                int wordCount = 0;
                Object obj = pageMap.get(forwardIndex.getPid());
                if (obj != null) {
                    wordCount = (Integer) obj;
                }
                if (docFrequency != 0 && wordCount != 0) {
                    double tf = forwardIndex.getMatchTimes() / (double) wordCount;
                    double idf = Math.log(5017805 / (double) docFrequency);
                    logger.info("tf:" + tf + ",idf:" + idf);
                    forwardIndex.setTfidf(tf * idf);
                    forwardIndexService.update(forwardIndex);
                }
            }
            logger.info("page No." + pageIndex + " succeeded!");
        }
    }
}
