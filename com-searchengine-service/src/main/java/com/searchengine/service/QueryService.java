package com.searchengine.service;

import com.searchengine.domain.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueryService {
    /**
     * 添加新的查询记录
     *
     * @param query
     * @return
     */
    long add(Query query);

    /**
     * 查询内容
     * @param queryContent
     * @return
     */
    Query get(String queryContent);

    /**
     * 分页得到查询记录
     *
     * @return
     */
    List<Query> getList(int pageIndex, int pageSize);

    /**
     * 得到查询记录总数
     *
     * @return
     */
    long getCount();
}
