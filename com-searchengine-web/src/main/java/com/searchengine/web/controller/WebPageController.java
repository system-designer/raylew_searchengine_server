package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.WebPage;
import com.searchengine.domain.dic.ResultCode;
import com.searchengine.domain.vo.Result;
import com.searchengine.service.WebPageService;
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
@RequestMapping("/api/webpage")
public class WebPageController extends ControllerBase {

    private static final Type WebPageType = new TypeToken<Result<ArrayList<WebPage>>>() {
    }.getType();

    @Autowired
    private WebPageService webPageService;

    /**
     * 分页得到网页信息
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getListBy(@RequestParam int pageIndex, @RequestParam int pageSize) {

        Result<List<WebPage>> result = new Result<List<WebPage>>();

        try {
            List<WebPage> webPageList = webPageService.getListBy(pageIndex, pageSize);
            result.setCode(ResultCode.USER_OK);
            result.setData(webPageList);
        } catch (Exception ex) {
            result.setCode(ResultCode.ERROR);
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, WebPageType);
    }

    /**
     * 得到网页总数
     *
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getCount() {
        Result<Long> result = new Result<Long>();

        try {
            Long count = webPageService.getCount();
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
