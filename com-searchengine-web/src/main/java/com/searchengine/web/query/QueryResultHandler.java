package com.searchengine.web.query;

import com.searchengine.domain.*;
import com.searchengine.service.*;
import com.searchengine.utils.FileAction;
import com.searchengine.utils.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Created by RayLew on 2015/2/16.
 */
public class QueryResultHandler {
    private final static int RANGE = 50;
    protected final Logger logger = Logger.getLogger(this.getClass());
    private ArrayList<QueryResult> results;
    private WordService wordService;
    private ForwardIndexService forwardIndexService;
    private PageWordService pageWordService;
    private WebPageService webPageService;
    private RedisService redisService;

    /**
     * 构造方法：服务注入
     *
     * @param wordService
     * @param forwardIndexService
     * @param pageWordService
     * @param webPageService
     * @param redisService
     */
    public QueryResultHandler(WordService wordService, ForwardIndexService forwardIndexService, PageWordService pageWordService, WebPageService webPageService, RedisService redisService) {
        this.wordService = wordService;
        this.forwardIndexService = forwardIndexService;
        this.pageWordService = pageWordService;
        this.webPageService = webPageService;
        this.redisService = redisService;
    }

    public ArrayList<QueryResult> getResponse(String request, int pageIndex, int pageSize) {
        doQuery3(request, pageIndex, pageSize);
        return results;
    }

    // 查询过程：
    // 1. 关键词分词、剔除停用词，并对分词结果进行查找对应的结果
    // 2. 合并各个分词的结果，返回初步的网页URL信息
    // 3. 根据URL通过数据库获得网页所在位置，从而在RAWs中获得网页内容
    // 4. 整合网页内容，剔除TAG等标签信息，创建该网页的Result对象
    // 5. 在JSP页面中显示结果列表，做出适当的分页
    // 6. 完成快照功能
    // 注意点：
    // 1. 考虑性能的问题，如果网页库比较大，很可能回到只查询的缓慢和资源的大量消耗
    // 2. 考虑网页的排名问题
    private void doQuery(String request) {
        //得到词表
        List<Word> wordList = null;
        Object obj = redisService.get("wordList");
        if (obj != null) {
            wordList = (List<Word>) obj;
        } else {
            wordList = wordService.getList();
            redisService.add("wordList", wordList);
        }
        // 1. 关键词分词、剔除停用词，并对分词结果进行查找对应的结果
        // 2. 合并各个分词的结果，返回初步的网页URL信息
        results = new ArrayList<QueryResult>();
        try {
            List<Long> wordIdList = new ArrayList<Long>();
            StringReader sr = new StringReader(request);
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            while ((lex = ik.next()) != null) {
                String wordStr = lex.getLexemeText();
                for (Word word : wordList) {
                    if (word.getName().equals(wordStr)) {
                        if (!wordIdList.contains(word.getId())) {
                            wordIdList.add(word.getId());
                        }
                        break;
                    }
                }
            }
            List<PageWord> pageWordList = pageWordService.getListByWordIdList(wordIdList);
            Map<Long, Double> pageWordMap = new LinkedHashMap<Long, Double>();
            if (pageWordList != null) {
                //考虑全匹配，通过tf-idf的总和倒序排列
                for (PageWord pageWord : pageWordList) {
                    if (pageWordMap.containsKey(pageWord.getPid())) {
                        double total_tf_idf = pageWordMap.get(pageWord.getPid());
                        pageWordMap.put(pageWord.getPid(), total_tf_idf + pageWord.getTfidf());
                    } else {
                        pageWordMap.put(pageWord.getPid(), pageWord.getTfidf());
                    }
                }
                //按照tfidf对网页进行倒序排序
                ArrayList<Map.Entry<Long, Double>> entryList = new ArrayList<Map.Entry<Long, Double>>(pageWordMap.entrySet());
                Collections.sort(entryList, new Comparator<Map.Entry<Long, Double>>() {
                    @Override
                    public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                        if ((o2.getValue() - o1.getValue()) >= 0.0) {
                            return 1;
                        }
                        return -1;
                    }
                });
                for (Map.Entry<Long, Double> entry : entryList) {
                    long pid = entry.getKey();
                    List<PageWord> matchedPageWordList = new ArrayList<PageWord>();
                    PageWord matchedPageWord = null;
                    for (PageWord pageWord : pageWordList) {
                        if (pageWord.getPid() == pid) {
                            if (matchedPageWord == null) {
                                matchedPageWord = pageWord;
                            }
                            matchedPageWordList.add(pageWord);
                        }
                    }
                    //单个网页中按匹配的词语位置对索引进行正序排序
                    Collections.sort(matchedPageWordList);
                    QueryResult queryResult = generateResult(matchedPageWordList);
                    if (queryResult != null) {
                        results.add(queryResult);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doQuery2(String request) {
        //得到词表
        List<Word> wordList = null;
        Object obj = redisService.get("wordList");
        if (obj != null) {
            wordList = (List<Word>) obj;
        } else {
            wordList = wordService.getList();
            redisService.add("wordList", wordList);
        }
        // 1. 关键词分词、剔除停用词，并对分词结果进行查找对应的结果
        // 2. 合并各个分词的结果，返回初步的网页URL信息
        results = new ArrayList<QueryResult>();
        try {
            List<Long> wordIdList = new ArrayList<Long>();
            StringReader sr = new StringReader(request);
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            while ((lex = ik.next()) != null) {
                String wordStr = lex.getLexemeText();
                for (Word word : wordList) {
                    if (word.getName().equals(wordStr)) {
                        if (!wordIdList.contains(word.getId())) {
                            wordIdList.add(word.getId());
                        }
                        break;
                    }
                }
            }
            List<PageWord> pageWordList = pageWordService.getListByWordIdList(wordIdList);
            Map<Long, Integer> pageWordMap = new LinkedHashMap<Long, Integer>();
            if (pageWordList != null) {
                //考虑全匹配，通过匹配的词数count倒序排列
                for (PageWord pageWord : pageWordList) {
                    if (pageWordMap.containsKey(pageWord.getPid())) {
                        int count = pageWordMap.get(pageWord.getPid());
                        pageWordMap.put(pageWord.getPid(), count + 1);
                    } else {
                        pageWordMap.put(pageWord.getPid(), 1);
                    }
                }
                //按照匹配词语个数对网页进行倒序排序
                ArrayList<Map.Entry<Long, Integer>> entryList = new ArrayList<Map.Entry<Long, Integer>>(pageWordMap.entrySet());
                Collections.sort(entryList, new Comparator<Map.Entry<Long, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                        return o2.getValue() - o1.getValue();
                    }
                });
                for (Map.Entry<Long, Integer> entry : entryList) {
                    long pid = entry.getKey();
                    List<PageWord> matchedPageWordList = new ArrayList<PageWord>();
                    PageWord matchedPageWord = null;
                    for (PageWord pageWord : pageWordList) {
                        if (pageWord.getPid() == pid) {
                            if (matchedPageWord == null) {
                                matchedPageWord = pageWord;
                            }
                            matchedPageWordList.add(pageWord);
                        }
                    }
                    //单个网页中按匹配的词语位置对索引进行正序排序
                    Collections.sort(matchedPageWordList);
                    QueryResult queryResult = generateResult(matchedPageWordList);
                    if (queryResult != null) {
                        results.add(queryResult);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doQuery3(String request, int pageIndex, int pageSize) {
        //得到词表
        List<Word> wordList = null;
        Object obj = redisService.get("wordList");
        if (obj != null) {
            wordList = (List<Word>) obj;
        } else {
            wordList = wordService.getList();
            redisService.add("wordList", wordList);
        }
        // 1. 关键词分词、剔除停用词，并对分词结果进行查找对应的结果
        // 2. 合并各个分词的结果，返回初步的网页URL信息
        results = new ArrayList<QueryResult>();
        try {
            List<Long> wordIdList = new ArrayList<Long>();
            StringReader sr = new StringReader(request);
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            while ((lex = ik.next()) != null) {
                String wordStr = lex.getLexemeText();
                for (Word word : wordList) {
                    if (word.getName().equals(wordStr)) {
                        if (!wordIdList.contains(word.getId())) {
                            wordIdList.add(word.getId());
                        }
                        break;
                    }
                }
            }
            if (wordIdList != null) {
                Map<Long, List<InvertedIndex>> invertedMap = new HashMap<Long, List<InvertedIndex>>();
                Map<Long, Double> tfidfMap = new LinkedHashMap<Long, Double>();
                int totalCount = 0;
                for (int i = 0; i < wordIdList.size(); i++) {
                    Long wordId = wordIdList.get(i);
                    List<InvertedIndex> invertedIndexList = (List<InvertedIndex>) redisService.get("index" + wordId);
                    if (invertedIndexList != null) {
                        for (InvertedIndex invertedIndex : invertedIndexList) {
                            if (tfidfMap.containsKey(invertedIndex.getPid())) {
                                double total_tf_idf = tfidfMap.get(invertedIndex.getPid());
                                tfidfMap.put(invertedIndex.getPid(), total_tf_idf + invertedIndex.getTfidf());
                            } else {
                                tfidfMap.put(invertedIndex.getPid(), invertedIndex.getTfidf());
                            }
                        }
                        totalCount += invertedIndexList.size();
                    }
                    invertedMap.put(wordId, invertedIndexList);
                }
                //按照tfidf对网页进行倒序排序
                ArrayList<Map.Entry<Long, Double>> entryList = new ArrayList<Map.Entry<Long, Double>>(tfidfMap.entrySet());
                Collections.sort(entryList, new Comparator<Map.Entry<Long, Double>>() {
                    @Override
                    public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                        if ((o2.getValue() - o1.getValue()) < 0) {
                            return -1;
                        } else if ((o2.getValue() - o1.getValue()) > 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                int count = -1;
                for (Map.Entry<Long, Double> entry : entryList) {
                    count++;
                    if (count < (pageIndex - 1) * pageSize) {
                        continue;
                    }
                    //分页获取结果
                    if (count == ((pageIndex - 1) * pageSize + pageSize)) {
                        break;
                    }
                    long pid = entry.getKey();
                    List<PageWord> matchedPageWordList = new ArrayList<PageWord>();
                    //遍历所有的invertedindex
                    for (Map.Entry<Long, List<InvertedIndex>> invertedIndexEntry : invertedMap.entrySet()) {
                        List<InvertedIndex> invertedIndexList = invertedIndexEntry.getValue();
                        for (InvertedIndex invertedIndex : invertedIndexList) {
                            if (invertedIndex.getPid() == pid) {
                                PageWord matchedPageWord = new PageWord();
                                matchedPageWord.setId(invertedIndex.getPid());
                                matchedPageWord.setPosition(invertedIndex.getPosition());
                                matchedPageWord.setTfidf(invertedIndex.getTfidf());
                                //加上page
                                WebPage webPage = webPageService.getById(invertedIndex.getPid());
                                matchedPageWord.setTitle(webPage.getTitle());
                                matchedPageWord.setCharset(webPage.getCharset());
                                matchedPageWord.setKeywords(webPage.getKeywords());
                                matchedPageWord.setDescription(webPage.getDescription());
                                matchedPageWord.setUrl(webPage.getUrl());
                                matchedPageWord.setSavePath(webPage.getSavePath());
                                matchedPageWord.setCreatedTime(webPage.getCreatedTime());
                                //加上word
                                matchedPageWord.setWid(invertedIndexEntry.getKey());
                                for (Word word : wordList) {
                                    if (word.getId() == invertedIndexEntry.getKey()) {
                                        matchedPageWord.setName(word.getName());
                                        break;
                                    }
                                }
                                matchedPageWordList.add(matchedPageWord);
                                break;
                            }
                        }
                    }
                    //单个网页中按匹配的词语位置对索引进行正序排序
                    Collections.sort(matchedPageWordList);
                    QueryResult queryResult = generateResult(matchedPageWordList);
                    if (queryResult != null) {
                        results.add(queryResult);
                    }
                    //将总条数保存在第一条结果中
                    results.get(0).setRank(totalCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造匹配内容
     *
     * @param matchedPageWordList 单个网页匹配的索引列表
     * @return
     */
    private QueryResult generateResult(List<PageWord> matchedPageWordList) {
        //得到当前网页实体
        PageWord pageWord = matchedPageWordList.get(0);
        String text = FileAction.convertFileToString(com.searchengine.utils.Constant.WEBPAGE_BASEPATH + "/" + pageWord.getSavePath(),
                pageWord.getCharset());
        Document document = Jsoup.parse(text);
        text = document.text();
        //分析索引，构造内容
        StringBuilder contentStringBuilder = new StringBuilder();
        contentStringBuilder.append("......");
        int startIndex = 0;
        int endIndex = 0;
        int size = matchedPageWordList.size();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                startIndex = 0;
            } else {
                PageWord prePageWord = matchedPageWordList.get(i - 1);
                startIndex = prePageWord.getPosition() + prePageWord.getName().length();
            }
            //当前索引
            PageWord curPageWord = matchedPageWordList.get(i);
            endIndex = curPageWord.getPosition();
            if ((endIndex - startIndex) > 2 * RANGE) {
                contentStringBuilder.append(text.substring(endIndex - RANGE, endIndex));
            } else {
                contentStringBuilder.append(text.substring(startIndex, endIndex));
            }
            //标记匹配词语
            contentStringBuilder.append("<span style='color:red;'>").append(curPageWord.getName()).append("</span>");
            startIndex = endIndex + curPageWord.getName().length();
            //下一个的索引
            if (i == (size - 1)) {
                endIndex = text.length();
            } else {
                PageWord nextPageWord = matchedPageWordList.get(i + 1);
                endIndex = nextPageWord.getPosition();
            }
            if ((endIndex - startIndex) > 2 * RANGE) {
                contentStringBuilder.append(text.substring(startIndex, startIndex + RANGE));
            } else {
                contentStringBuilder.append(text.substring(startIndex, endIndex));
            }
            if ((endIndex - startIndex) != 0) {
                contentStringBuilder.append("......");
            }
        }
        QueryResult queryResult = new QueryResult(generateTitleResult(matchedPageWordList), contentStringBuilder.toString(),
                pageWord.getUrl(), pageWord.getSavePath(), pageWord.getCharset(), pageWord.getCreatedTime().toString());
        return queryResult;
    }

    /**
     * 构造匹配title内容
     *
     * @param matchedPageWordList 单个网页匹配的索引列表
     * @return
     */
    private String generateTitleResult(List<PageWord> matchedPageWordList) {
        //得到当前网页实体
        PageWord pageWord = matchedPageWordList.get(0);
        String title = pageWord.getTitle();
        if (StringUtils.isNotEmpty(title)) {
            int size = matchedPageWordList.size();
            for (int i = 0; i < size; i++) {
                PageWord curPageWord = matchedPageWordList.get(i);
                //标记匹配词语
                if (title.contains(curPageWord.getName())) {
                    title = title.replaceAll(curPageWord.getName(), "<span style='color:red;'>" + curPageWord.getName() + "</span>");
                }
            }
        }
        return title;
    }
}
