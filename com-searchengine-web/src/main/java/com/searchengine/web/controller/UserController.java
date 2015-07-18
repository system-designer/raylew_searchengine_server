package com.searchengine.web.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.domain.dic.ResultCode;
import com.searchengine.domain.vo.Result;
import com.searchengine.domain.vo.UserInfoVO;
import com.searchengine.service.UserInfoService;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * 用户账户控制器
 * User: RayLew
 * Date: 14-11-7
 * Time: 下午7:30
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST}, produces = {CommonUtils.MediaTypeJSON})
    @ResponseBody()
    public String get(HttpServletRequest request, HttpServletResponse response) {

        Result<UserInfoVO> result = new Result<UserInfoVO>();
        String userId = request.getParameter("id");
        if (StringUtils.isEmpty(userId) || !StringUtils.isNumeric(userId)) {
            result.setCode(ResultCode.ERROR);
            result.setMsg("parameter id is invalid");

        } else {
            UserInfoVO userInfo = userInfoService.getById(Long.valueOf(userId));
            if (userInfo != null) {
                result.setCode(ResultCode.SUCCESS);
                result.setMsg("success");
                result.setTimestamp(new Date());
//                UserInfoVO infoVo = TransformUtils.toUserInfoVo(userInfo);
                result.setData(userInfo);
                Type listType2 = new TypeToken<Result<UserInfoVO>>() {
                }.getType();
                return JsonUtils.toJson(result, listType2);
            }
        }
        return JsonUtils.toJson(result);
    }
}
