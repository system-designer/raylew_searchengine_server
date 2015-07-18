package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.PageWord;
import com.searchengine.domain.dic.ResultCode;
import com.searchengine.domain.vo.Result;
import com.searchengine.service.PageWordService;
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
@RequestMapping("/api/pageword")
public class PageWordController extends ControllerBase {

    private static final Type PageWordType = new TypeToken<Result<ArrayList<PageWord>>>() {
    }.getType();

    @Autowired
    private PageWordService pageWordService;

    /**
     * 得到索引列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getListBy(@RequestParam int pageIndex, @RequestParam int pageSize) {

        Result<List<PageWord>> result = new Result<List<PageWord>>();

        try {
            List<PageWord> pageWordList = pageWordService.getListBy(pageIndex, pageSize);
            result.setCode(ResultCode.USER_OK);
            result.setData(pageWordList);
        } catch (Exception ex) {
            result.setCode(ResultCode.ERROR);
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, PageWordType);
    }

    /**
     * 得到索引总数
     *
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getCount() {
        Result<Long> result = new Result<Long>();

        try {
            Long count = pageWordService.getCount();
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
