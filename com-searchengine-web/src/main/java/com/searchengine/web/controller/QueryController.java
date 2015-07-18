package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.Query;
import com.searchengine.domain.QueryResult;
import com.searchengine.domain.UserQuery;
import com.searchengine.domain.dic.ResultCode;
import com.searchengine.domain.vo.Result;
import com.searchengine.service.*;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import com.searchengine.web.query.QueryResponse;
import com.searchengine.web.query.QueryResultHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/query")
public class QueryController extends ControllerBase {

    private static final Type QueryResultType = new TypeToken<Result<ArrayList<QueryResult>>>() {
    }.getType();

    @Autowired
    private WordService wordService;

    @Autowired
    private ForwardIndexService forwardIndexService;

    @Autowired
    private PageWordService pageWordService;

    @Autowired
    private WebPageService webPageService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private UserQueryService userQueryService;

    /**
     * @param queryContent
     * @return
     * @Deprecated 获取网页内容
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST}, produces = {CommonUtils.MediaTypeJSON})
    @ResponseBody
    @Deprecated
    public String run(@RequestParam String queryContent) {
        Result<ArrayList<QueryResult>> result = new Result<ArrayList<QueryResult>>();
        if (!StringUtils.isNotEmpty(queryContent)) {
            queryContent = "";
            result.setCode(ResultCode.ERROR);
        } else {
            try {
                queryContent = URLDecoder.decode(queryContent, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
            } finally {
            }
        }
        QueryResponse queryResponse = new QueryResponse(redisService);
        ArrayList<QueryResult> results = queryResponse.getResponse(queryContent);
        result.setCode(ResultCode.SUCCESS);
        result.setData(results);
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, QueryResultType);
    }

    /**
     * 获取查询结果
     *
     * @param queryContent
     * @return
     */
    @RequestMapping(value = "/betaList", method = {RequestMethod.GET, RequestMethod.POST}, produces = {CommonUtils.MediaTypeJSON})
    @ResponseBody
    public String betaList(@RequestParam("queryContent") String queryContent, @RequestParam("pageIndex") int pageIndex,
                           @RequestParam("pageSize") int pageSize, @RequestParam("userId") Long userId) {
        Result<ArrayList<QueryResult>> result = new Result<ArrayList<QueryResult>>();
        if (!StringUtils.isNotEmpty(queryContent)) {
            queryContent = "";
            result.setCode(ResultCode.ERROR);
        } else {
            try {
                queryContent = URLDecoder.decode(queryContent, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
            } finally {
            }
        }
        //添加用户查询记录
        Query query = queryService.get(queryContent);
        Long qid = 0L;
        if (query == null) {
            query = new Query();
            query.setContent(queryContent);
            qid = queryService.add(query);
        } else {
            qid = query.getId();
        }
        if (qid > 0 && userId > 0) {
            UserQuery userQuery = new UserQuery();
            userQuery.setQid(qid);
            userQuery.setUid(userId);
            userQueryService.add(userQuery);
        }
        //加上缓存策略
        ArrayList<QueryResult> results = null;
        if (pageIndex == 1) {
            Object obj = redisService.get(queryContent);
            if (obj == null) {
                QueryResultHandler queryResultHandler = new QueryResultHandler(wordService, forwardIndexService, pageWordService, webPageService, redisService);
                results = queryResultHandler.getResponse(queryContent, pageIndex, pageSize);
                //将首页结果缓存起来
                redisService.add(queryContent, results);
            } else {
                results = (ArrayList<QueryResult>) obj;
                //redisService.remove(queryContent);
            }
        } else {
            QueryResultHandler queryResultHandler = new QueryResultHandler(wordService, forwardIndexService, pageWordService, webPageService, redisService);
            results = queryResultHandler.getResponse(queryContent, pageIndex, pageSize);
        }
        result.setCode(ResultCode.SUCCESS);
        result.setData(results);
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, QueryResultType);
    }

    /**
     * 获取某个日期下的最热搜索
     *
     * @param date 日期
     * @return
     */
    @RequestMapping(value = "/hotQueryList", method = {RequestMethod.GET}, produces = {CommonUtils.MediaTypeJSON})
    @ResponseBody
    public String dailyHits(@RequestParam("date") String date) {
        Result<List<UserQuery>> result = new Result<List<UserQuery>>();
        List<UserQuery> userQueryList = userQueryService.getList(date);
        result.setCode(ResultCode.SUCCESS);
        result.setData(userQueryList);
        result.setTimestamp(new Date());
        Type userQueryType = new TypeToken<Result<ArrayList<UserQuery>>>() {
        }.getType();
        return JsonUtils.toJson(result, userQueryType);
    }
}
