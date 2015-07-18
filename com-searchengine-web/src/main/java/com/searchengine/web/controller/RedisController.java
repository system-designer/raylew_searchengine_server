package com.searchengine.web.controller;

import com.searchengine.domain.BaseEntity;
import com.searchengine.domain.WebPage;
import com.searchengine.domain.vo.Result;
import com.searchengine.service.RedisService;
import com.searchengine.service.WebPageService;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/api/redis")
public class RedisController extends ControllerBase {

    @Autowired
    private RedisService redisService;

    @Autowired
    private WebPageService webPageService;

    /**
     * 取得redis的值
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getBaseEntity", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getBaseEntity(HttpServletRequest request) {
        Result<BaseEntity> result = new Result<BaseEntity>();
        long id = 1017221982732288l;
        WebPage webPage = webPageService.getById(id);
        redisService.add(String.valueOf(webPage.getId()), webPage);
        WebPage webPage1 = (WebPage) redisService.get(String.valueOf(webPage.getId()));
        if (webPage1 != null) {
            result.setData(webPage1);
        } else {
            result.setData(null);
        }
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.BaseEntityType);
    }

    /**
     * 清除redis的值
     *
     * @return
     */
    @RequestMapping(value = "/getValue", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String getBaseEntity(@RequestParam String key) {
        Result<String> result = new Result<String>();
        String res = "nothing";
        Object obj = redisService.get(key);
        if (obj != null) {
            res = (String) obj;
        }
        result.setData(res);
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.StringType);
    }
}
