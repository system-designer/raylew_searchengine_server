package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.Word;
import com.searchengine.domain.dic.ResultCode;
import com.searchengine.domain.vo.Result;
import com.searchengine.service.WordService;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/word")
public class WordController extends ControllerBase {

    private static final Type WordType = new TypeToken<Result<ArrayList<Word>>>() {
    }.getType();

    @Autowired
    private WordService wordService;

    /**
     * 得到词库列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getListBy(@RequestParam int pageIndex, @RequestParam int pageSize) {

        Result<List<Word>> result = new Result<List<Word>>();

        try {
            List<Word> wordList = wordService.getListBy(pageIndex, pageSize);
            result.setCode(ResultCode.USER_OK);
            result.setData(wordList);
        } catch (Exception ex) {
            result.setCode(ResultCode.ERROR);
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, WordType);
    }

    /**
     * 得到词语总数
     *
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getCount() {
        Result<Long> result = new Result<Long>();

        try {
            Long count = wordService.getCount();
            result.setCode(ResultCode.USER_OK);
            result.setData(count);
        } catch (Exception ex) {
            result.setCode(ResultCode.ERROR);
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        Type longType = new TypeToken<Result<Long>>() {
        }.getType();
        return JsonUtils.toJson(result, longType);
    }
}
