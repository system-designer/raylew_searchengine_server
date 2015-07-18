package com.searchengine.service.impl;

import com.searchengine.dao.UserInfoDao;
import com.searchengine.domain.UserInfo;
import com.searchengine.domain.vo.UserInfoVO;
import com.searchengine.service.UserInfoService;
import com.searchengine.service.UserPasswordService;
import com.searchengine.utils.BeanUtils;
import com.searchengine.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户账户基本信息服务管理
 * User: Ray Lew
 * Date: 14-11-7
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceBase implements UserInfoService {

    @Resource
    private UserInfoDao userInfoDao;

    @Autowired
    private UserPasswordService userPasswordService;

    /*@Resource
    private PassportService passportService;*/

    /**
     * 添加用户
     *
     * @param userInfoVO
     * @return
     */
    @Override
    public long add(UserInfoVO userInfoVO) {

        // 检查用户是否存在
        boolean userExists = exists(userInfoVO.getLoginName());
        if (userExists) {
            return 0;
        }

        // 创建Id
        long id = userInfoVO.getId() <= 0 ? GenerateId() : userInfoVO.getId();
        String salt = userPasswordService.createSalt();
        String passwordEncoded = userPasswordService.passwordEncode(userInfoVO.getPassword(), salt);
        Date nowDate = new Date();

        UserInfo userInfo = BeanUtils.copyProperties(userInfoVO, UserInfo.class);
        userInfo.setId(id);
        userInfo.setPassword(passwordEncoded);
        userInfo.setSalt(salt);
        userInfo.setCreatedTime(nowDate);
        userInfo.setLastUpdatedTime(nowDate);

        int ret = userInfoDao.add(userInfo);
        if (ret > 0) {
            return id;
        }

        return 0;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public UserInfoVO getById(long id) {
        UserInfo userInfo = findBy(id);
        if (userInfo == null) {
            return null;
        }

        UserInfoVO userInfoVO = BeanUtils.copyProperties(userInfo, UserInfoVO.class);
        userInfoVO.setPassword(null);
        return userInfoVO;
    }

    @Override
    public UserInfoVO getByPassportId(long passportId) {
        UserInfo userInfo = userInfoDao.getByPassportId(passportId);
        if (userInfo == null) {
            return null;
        }

        UserInfoVO userInfoVO = BeanUtils.copyProperties(userInfo, UserInfoVO.class);
        userInfoVO.setPassword(null);
        return userInfoVO;
    }

    @Override
    public UserInfoVO getByLoginName(String loginName) {
        UserInfo userInfo = findBy(loginName);
        if (userInfo == null) {
            return null;
        }

        UserInfoVO userInfoVO = BeanUtils.copyProperties(userInfo, UserInfoVO.class);
        userInfoVO.setPassword(null);
        return userInfoVO;
    }

    @Override
    public UserInfoVO getByLoginNameAndPassword(String loginName, String password) {
        UserInfo userInfo = findBy(loginName);
        if (userInfo == null) {
            return null;
        }

        // 进行密码编码
        String salt = userInfo.getSalt();
        String passwordEncode = userPasswordService.passwordEncode(password, salt);
        if (!passwordEncode.equals(userInfo.getPassword())) {
            return null;
        }

        UserInfoVO userInfoVO = BeanUtils.copyProperties(userInfo, UserInfoVO.class);
        userInfoVO.setPassword(null);
        return userInfoVO;
    }

    /**
     * 更新
     *
     * @param userInfoVO
     * @return
     */
    @Override
    public int update(UserInfoVO userInfoVO) {
        if (userInfoVO == null) {
            return 0;
        }

        UserInfo userInfo = BeanUtils.copyProperties(userInfoVO, UserInfo.class);

        return userInfoDao.update(userInfo);
    }

    /**
     * @param id
     * @return
     */
    private UserInfo findBy(long id) {
        return userInfoDao.getById(id);
    }

    /**
     * @param loginName
     * @return
     */
    private UserInfo findBy(String loginName) {
        return userInfoDao.getByLoginName(loginName);
    }

    /**
     * 是否存在
     *
     * @param loginName
     * @return
     */
    @Override
    public boolean exists(String loginName) {

        if (StringUtils.isEmpty(loginName)) {
            return true;
        }

        UserInfo user = findBy(loginName);
        if (user == null) {
            return false;
        }

        return true;
    }
}
