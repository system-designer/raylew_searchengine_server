package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.WebPage;
import com.searchengine.domain.vo.PageIndexVO;
import com.searchengine.domain.vo.Result;
import com.searchengine.service.PageIndexService;
import com.searchengine.service.WebPageService;
import com.searchengine.spider.beta.Spider;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/spider")
public class SpiderController extends ControllerBase {

    private static final Type PageIndexVOType = new TypeToken<Result<PageIndexVO>>() {
    }.getType();

    @Autowired
    private PageIndexService pageIndexService;

    @Autowired
    private WebPageService webPageService;

    /**
     * 运行爬虫
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/run", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String run(HttpServletRequest request) {
        Result<Long> result = new Result<Long>();

        ArrayList<URL> urls = new ArrayList<URL>();
        ArrayList<URL> visitedUrls = new ArrayList<URL>();
        try {
            urls.add(new URL("http://www.hbnu.info"));
            urls.add(new URL("http://news.sohu.com"));
            urls.add(new URL("http://news.qq.com"));
            urls.add(new URL("http://news.baidu.com"));
            urls.add(new URL("http://news.sina.com.cn"));
            urls.add(new URL("http://news.163.com"));
            List<WebPage> webPageList = webPageService.getList(null);
            if (webPageList != null) {
                for (WebPage webPage : webPageList) {
                    visitedUrls.add(new URL(webPage.getUrl()));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Spider spider = new Spider(urls,webPageService);
        Spider spider = new Spider(urls, visitedUrls, webPageService);
        spider.start();

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.LongType);
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String stop(HttpServletRequest request) {
        Result<Long> result = new Result<Long>();

        return JsonUtils.toJson(result, ResponseResultType.LongType);
    }
}
