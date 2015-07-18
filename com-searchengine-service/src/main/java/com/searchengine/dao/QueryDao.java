package com.searchengine.dao;

import com.searchengine.domain.Query;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by RayLew on 2015/2/4.
 * QQ:897929321
 */
public interface QueryDao {
    /**
     * 添加查询记录
     *
     * @param query
     * @return
     */
    int add(Query query);

    /**
     *
     * @return
     */
    Query get(@Param("content") String content);

    /**
     * 分页得到查询的记录
     *
     * @return
     */
    List<Query> getList(@Param("offset") int offset, @Param("size") int size);

    /**
     * 得到查询记录总数
     *
     * @return
     */
    Long getCount();
}
