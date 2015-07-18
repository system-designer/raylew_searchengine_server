package com.searchengine.preprocess.beta.forwardIndex;

import com.searchengine.domain.WebPage;
import com.searchengine.domain.Word;
import com.searchengine.service.ForwardIndexService;
import com.searchengine.service.WebPageService;
import com.searchengine.service.WordService;

import java.util.List;

public class ForwardIndexHandler {
    private WordService wordService;
    private int threadNum = 16;
    private ForwardIndexService forwardIndexService;
    private WebPageService webPageService;

    /**
     * 构造方法：服务注入
     *
     * @param wordService
     * @param forwardIndexService
     * @param webPageService
     */
    public ForwardIndexHandler(WordService wordService, ForwardIndexService forwardIndexService, WebPageService webPageService) {
        this.wordService = wordService;
        this.forwardIndexService = forwardIndexService;
        this.webPageService = webPageService;
    }

    /**
     * 建立正排索引
     *
     * @return
     */
    public void createForwardIndex() {
        List<Word> wordList = wordService.getList();
        List<WebPage> webPageList = webPageService.getWebPageList(Boolean.TRUE);
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(new ForwardIndexThread(i, forwardIndexService,webPageService, wordList, webPageList));
            thread.start();
        }
    }
}
