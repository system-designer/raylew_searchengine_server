package com.searchengine.dao;

import com.searchengine.domain.Query;
import com.searchengine.domain.UserQuery;
import com.searchengine.domain.Word;
import com.sun.jmx.snmp.UserAcl;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by RayLew on 2015/2/4.
 * QQ:897929321
 */
public interface UserQueryDao {
    /**
     * 添加用户查询记录
     *
     * @param userQuery
     * @return
     */
    int add(UserQuery userQuery);

    /**
     * 分页得到用户查询的记录
     *
     * @return
     */
    List<UserQuery> getList(@Param("userId") Long userId,@Param("offset") int offset, @Param("size") int size);

    /**
     * 得到某个日期下最热的搜索
     *
     * @return
     */
    List<UserQuery> getHotQuery(@Param("date") String date);

    /**
     * 得到用户查询的记录总数
     *
     * @return
     */
    Long getCount(Long userId);
}
