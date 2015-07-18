package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.dic.ResultCode;
import com.searchengine.domain.vo.PageIndexVO;
import com.searchengine.domain.vo.Result;
import com.searchengine.service.PageIndexService;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Date;

@Controller
@RequestMapping("/api/pageIndex")
@Deprecated
public class PageIndexController extends ControllerBase {

    private static final Type PageIndexVOType = new TypeToken<Result<PageIndexVO>>() {
    }.getType();

    @Autowired
    private PageIndexService pageIndexService;

    /**
     * 新增页面索引
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String add(HttpServletRequest request) {

        Result<Long> result = new Result<Long>();

        try {
            String json = getRequestEntity(request);
            PageIndexVO pageIndexVO = JsonUtils.fromJson(json, PageIndexVO.class);
            if (pageIndexVO != null) {
                long pageIndexId = pageIndexService.add(pageIndexVO);
                if (pageIndexId > 0) {
                    result.setCode(ResultCode.USER_OK);
                    result.setData(pageIndexId);
                } else {
                    result.setCode(ResultCode.USER_FAILURE);
                    result.setMsg("id冲突！");
                }
            } else {
                result.setCode(ResultCode.ERROR);
                result.setMsg("参数错误！");
            }
        } catch (Exception ex) {
            result.setMsg("创建失败！");
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.LongType);
    }

    /**
     * 获取页面信息
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String get(HttpServletRequest request) {
        Result<PageIndexVO> result = new Result<PageIndexVO>();
        String idStr = request.getParameter("id");
        try {
            PageIndexVO pageIndexVO = null;
            if (!StringUtils.isEmpty(idStr)) {
                long id = Long.parseLong(idStr);
                pageIndexVO = pageIndexService.getById(id);
            }
            if (pageIndexVO == null) {
                result.setCode(ResultCode.USER_NOT_EXIST);
                result.setMsg("不存在！");
            } else {
                result.setCode(ResultCode.USER_OK);
            }
            result.setData(pageIndexVO);
        } catch (Exception ex) {
            logger.error(ex);
        }
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, PageIndexVOType);
    }
}
