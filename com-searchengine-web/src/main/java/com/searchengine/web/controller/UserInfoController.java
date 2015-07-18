package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.dic.ResultCode;
import com.searchengine.domain.vo.Result;
import com.searchengine.domain.vo.UserInfoVO;
import com.searchengine.service.UserInfoService;
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
@RequestMapping("/api/user")
public class UserInfoController extends ControllerBase {

    private static final Type UserInfoVOType = new TypeToken<Result<UserInfoVO>>() {
    }.getType();

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 新增用户
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String add(HttpServletRequest request) {

        Result<Long> result = new Result<Long>();

        try {
            String json = getRequestEntity(request);
            UserInfoVO userInfoVO = JsonUtils.fromJson(json, UserInfoVO.class);
            if (userInfoVO != null) {
                long userId = userInfoService.add(userInfoVO);
                if (userId > 0) {
                    result.setCode(ResultCode.USER_OK);
                    result.setData(userId);
                } else {
                    result.setCode(ResultCode.USER_FAILURE);
                    result.setMsg("用户名冲突！");
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
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String get(HttpServletRequest request) {
        Result<UserInfoVO> result = new Result<UserInfoVO>();

        String id = request.getParameter("id");
        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");

        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(loginName)) {
            result.setCode(ResultCode.ERROR);
            result.setMsg("id和loginName不能都为空！");
        }

        try {
            UserInfoVO userInfoVO = null;

            if (!StringUtils.isEmpty(id)) {
                long userId = Long.parseLong(id);
                userInfoVO = userInfoService.getById(userId);
            } else if (!StringUtils.isEmpty(loginName)) {

                if (StringUtils.isEmpty(password)) {
                    userInfoVO = userInfoService.getByLoginName(loginName);
                } else {
                    userInfoVO = userInfoService.getByLoginNameAndPassword(loginName, password);
                }
            }

            if (userInfoVO == null) {
                result.setCode(ResultCode.USER_NOT_EXIST);
                result.setMsg("用户不存在！");
            } else {
                result.setCode(ResultCode.USER_OK);
            }

            result.setData(userInfoVO);
        } catch (Exception ex) {
            logger.error(ex);
        }

        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, UserInfoVOType);
    }
}
