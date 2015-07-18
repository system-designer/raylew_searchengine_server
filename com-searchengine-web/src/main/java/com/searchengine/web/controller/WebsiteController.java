package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.WebPage;
import com.searchengine.domain.Website;
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
@RequestMapping("/api/website")
public class WebsiteController extends ControllerBase {

    private static final Type WebsiteType = new TypeToken<Result<ArrayList<Website>>>() {
    }.getType();

    private static final Type WebPageType = new TypeToken<Result<ArrayList<WebPage>>>() {
    }.getType();

    @Autowired
    private WebPageService webPageService;

    /**
     * 分页得到网站信息
     *
     * @param domain
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getWebsiteList(@RequestParam String domain, @RequestParam int pageIndex, @RequestParam int pageSize) {

        Result<List<Website>> result = new Result<List<Website>>();

        try {
            List<Website> websiteList = webPageService.getWebsiteList(domain, pageIndex, pageSize);
            result.setCode(ResultCode.USER_OK);
            result.setData(websiteList);
        } catch (Exception ex) {
            result.setCode(ResultCode.ERROR);
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, WebsiteType);
    }

    /**
     * 得到网站总数
     *
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getWebsiteCount() {
        Result<Long> result = new Result<Long>();

        try {
            Long count = webPageService.getWebsiteCount();
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

    /**
     * 通过域名分页得到网页信息
     *
     * @param domain
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "webpagelist", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getListByDomain(@RequestParam String domain, @RequestParam int pageIndex, @RequestParam int pageSize) {

        Result<List<WebPage>> result = new Result<List<WebPage>>();

        try {
            List<WebPage> webPageList = webPageService.getListByDomain(domain, pageIndex, pageSize);
            result.setCode(ResultCode.USER_OK);
            result.setData(webPageList);
        } catch (Exception ex) {
            result.setCode(ResultCode.ERROR);
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, WebPageType);
    }
}
