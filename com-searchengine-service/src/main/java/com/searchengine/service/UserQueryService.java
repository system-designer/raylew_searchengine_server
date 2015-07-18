package com.searchengine.service;

import com.searchengine.domain.UserQuery;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface UserQueryService {
    /**
     * 添加新的用户查询记录
     *
     * @param userQuery
     * @return
     */
    long add(UserQuery userQuery);

    /**
     * 分页得到用户的查询记录
     *
     * @return
     */
    List<UserQuery> getList(Long userId, int pageIndex, int pageSize);

    /**
     * 得到某个日期下最热的搜索
     *
     * @return
     */
    List<UserQuery> getList(String date);

    /**
     * 得到用户查询记录总数
     *
     * @return
     */
    long getCount(Long userId);
}
