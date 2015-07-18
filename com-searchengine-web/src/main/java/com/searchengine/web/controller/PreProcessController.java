package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.ForwardIndex;
import com.searchengine.domain.InvertedIndex;
import com.searchengine.domain.vo.Result;
import com.searchengine.preprocess.alpha.index.RawsAnalyzer;
import com.searchengine.preprocess.beta.dic.DicUtil;
import com.searchengine.preprocess.beta.dic.WordSegHandler;
import com.searchengine.preprocess.beta.forwardIndex.ForwardIndexHandler;
import com.searchengine.preprocess.beta.invertedIndex.InvertedIndexHandler;
import com.searchengine.preprocess.beta.invertedIndex.TFIDFHandler;
import com.searchengine.service.*;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/preprocess")
public class PreProcessController extends ControllerBase {
    protected final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private PageIndexService pageIndexService;

    @Autowired
    private WordService wordService;

    @Autowired
    private ForwardIndexService forwardIndexService;

    @Autowired
    private WebPageService webPageService;

    @Autowired
    private InvertedIndexService invertedIndexService;

    @Autowired
    private RedisService redisService;

    /**
     * 运行预处理，该方法已过时
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/run", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String run() {
        Result<Long> result = new Result<Long>();

        RawsAnalyzer analyzer = new RawsAnalyzer("D:/Raws");
        analyzer.createPageIndex(pageIndexService);

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.LongType);
    }

    /**
     * 导入词典
     *
     * @return
     */
    @RequestMapping(value = "/importDic", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String importDic() {
        Result<Boolean> result = new Result<Boolean>();
        DicUtil dicUtil = new DicUtil();
        dicUtil.importMainWordDicToDB(wordService);
        result.setData(Boolean.TRUE);
        Type booleanType = new TypeToken<Result<Boolean>>() {
        }.getType();
        return JsonUtils.toJson(result, booleanType);
    }

    /**
     * 返回中文分词结果
     *
     * @return
     */
    @RequestMapping(value = "/getWordSegs", method = {RequestMethod.GET, RequestMethod.POST}, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getWordSegs(@RequestParam String text) {
        Result<String> result = new Result<String>();
        result.setData(WordSegHandler.getWordSegs(text));
        result.setMsg("成功");
        result.setTimestamp(new Date());
        Type stringType = new TypeToken<Result<String>>() {
        }.getType();
        return JsonUtils.toJson(result, stringType);
    }

    /**
     * 创建正排索引
     *
     * @return
     */
    @RequestMapping(value = "/createForwardIndex", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String createForwardIndex() {
        Result<Boolean> result = new Result<Boolean>();
        ForwardIndexHandler forwardIndexHandler = new ForwardIndexHandler(wordService, forwardIndexService, webPageService);
        forwardIndexHandler.createForwardIndex();
        result.setData(Boolean.TRUE);
        Type booleanType = new TypeToken<Result<Boolean>>() {
        }.getType();
        return JsonUtils.toJson(result, booleanType);
    }

    /**
     * 标记已经建立索引的页面
     *
     * @return
     */
    @RequestMapping(value = "/markWebPage", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String markWebPage() {
        Result<Boolean> result = new Result<Boolean>();
        webPageService.markWebPage();
        result.setData(Boolean.TRUE);
        Type booleanType = new TypeToken<Result<Boolean>>() {
        }.getType();
        return JsonUtils.toJson(result, booleanType);
    }

    /**
     * 计算tf-idf
     *
     * @return
     */
    @RequestMapping(value = "/calculateTFIDF", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String calculateTFIDF() {
        Result<Boolean> result = new Result<Boolean>();
        TFIDFHandler tfidfHandler = new TFIDFHandler(forwardIndexService, wordService, webPageService);
        tfidfHandler.calculateTFIDF();
        result.setData(Boolean.TRUE);
        Type booleanType = new TypeToken<Result<Boolean>>() {
        }.getType();
        return JsonUtils.toJson(result, booleanType);
    }

    /**
     * 得到正排索引列表
     *
     * @return
     */
    @RequestMapping(value = "/getForwardIndexList", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getForwardIndexList(@RequestParam("size") int size) {
        Result<Boolean> result = new Result<Boolean>();
        List<ForwardIndex> forwardIndexList = forwardIndexService.getList(1, size);
        logger.info(forwardIndexList.size());
        result.setData(Boolean.TRUE);
        Type booleanType = new TypeToken<Result<Boolean>>() {
        }.getType();
        return JsonUtils.toJson(result, booleanType);
    }

    /**
     * 创建倒排索引
     *
     * @return
     */
    @RequestMapping(value = "/createInvertedIndex", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String createInvertedIndex() {
        Result<Boolean> result = new Result<Boolean>();
        InvertedIndexHandler invertedIndexHandler = new InvertedIndexHandler(wordService, invertedIndexService, redisService);
        invertedIndexHandler.createInvertedIndex();
        result.setData(Boolean.TRUE);
        Type booleanType = new TypeToken<Result<Boolean>>() {
        }.getType();
        return JsonUtils.toJson(result, booleanType);
    }

    /**
     * 得到倒排索引列表
     *
     * @return
     */
    @RequestMapping(value = "/getInvertedIndexList", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getInvertedIndexList(@RequestParam("wid") Long wid) {
        Result<Integer> result = new Result<Integer>();
        List<InvertedIndex> invertedIndexList = invertedIndexService.getList(wid);
        logger.info(invertedIndexList.size());
        result.setData(invertedIndexList.size());
        Type integerType = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, integerType);
    }
}
