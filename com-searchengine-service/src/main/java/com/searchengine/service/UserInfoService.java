package com.searchengine.service;

import com.searchengine.domain.vo.UserInfoVO;
import org.springframework.stereotype.Service;

/**
 * 关于用户基本信息的操作
 * User: Ray Lew
 * Date: 14-11-7
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface UserInfoService {

    /**
     * 添加记录
     *
     * @param userInfoVO
     * @return
     */
    long add(UserInfoVO userInfoVO);

    /**
     * 根据Id查询用户账号信息
     *
     * @param id
     * @return
     */
    UserInfoVO getById(long id);

    /**
     * 根据PassportId查询用户账号信息
     *
     * @param passportId
     * @return
     */
    UserInfoVO getByPassportId(long passportId);

    /**
     * 根据登录名查找
     *
     * @param loginName
     * @return
     */
    UserInfoVO getByLoginName(String loginName);

    /**
     * @param loginName
     * @param password
     * @return
     */
    UserInfoVO getByLoginNameAndPassword(String loginName, String password);

    /**
     * 更新
     *
     * @param userInfoVO
     * @return
     */
    int update(UserInfoVO userInfoVO);

    /**
     * 是否存在
     *
     * @param longName
     * @return
     */
    boolean exists(String longName);
}
