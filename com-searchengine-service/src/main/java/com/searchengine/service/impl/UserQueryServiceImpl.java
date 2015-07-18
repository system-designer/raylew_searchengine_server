package com.searchengine.service.impl;

import com.searchengine.dao.UserQueryDao;
import com.searchengine.domain.UserQuery;
import com.searchengine.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service("userQueryService")
public class UserQueryServiceImpl extends ServiceBase implements UserQueryService {

    @Autowired
    private UserQueryDao userQueryDao;

    @Override
    public long add(UserQuery userQuery) {
        long id = GenerateId();
        userQuery.setId(id);
        Date nowDate = new Date();
        userQuery.setCreatedTime(nowDate);
        int ret = userQueryDao.add(userQuery);
        if (ret > 0) {
            return id;
        }
        return 0;
    }

    @Override
    public List<UserQuery> getList(Long userId, int pageIndex, int pageSize) {
        List<UserQuery> userQueryList = userQueryDao.getList(userId, (pageIndex - 1) * pageSize, pageSize);
        return userQueryList;
    }

    @Override
    public List<UserQuery> getList(String date) {
        List<UserQuery> userQueryList = userQueryDao.getHotQuery(date);
        return userQueryList;
    }

    @Override
    public long getCount(Long userId) {
        return userQueryDao.getCount(userId);
    }
}
