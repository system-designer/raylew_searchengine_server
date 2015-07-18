package com.searchengine.dao;

import com.searchengine.domain.WebPage;
import com.searchengine.domain.Website;
import com.sun.istack.internal.Nullable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by RayLew on 2015/2/4.
 */
public interface WebPageDao {
    /**
     * 添加页面
     *
     * @param webPage
     * @return
     */
    int add(WebPage webPage);

    /**
     * 通过id得到页面
     *
     * @param id
     * @return
     */
    WebPage getById(long id);

    /**
     * 通过url得到页面
     *
     * @param url
     * @return
     */
    WebPage getByUrl(String url);

    /**
     * 通过是否建立索引筛选页面
     *
     * @param analyzed
     * @return
     */
    List<WebPage> getList(@Param("analyzed") @Nullable Boolean analyzed);

    /**
     * 分页得到网站信息
     *
     * @param domain
     * @param offset
     * @param pageSize
     * @return
     */
    List<Website> getWebsiteList(@Param("domain") String domain, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 得到网站总数
     *
     * @return
     */
    Long getWebsiteCount();

    /**
     * 通过id列表得到页面
     *
     * @param idList
     * @return
     */
    List<WebPage> getListByIdList(List<Long> idList);

    /**
     * 分页得到页面
     *
     * @param offset
     * @param pageSize
     * @return
     */
    List<WebPage> getListBy(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 通过域名分页得到页面
     *
     * @param domain
     * @param offset
     * @param pageSize
     * @return
     */
    List<WebPage> getListByDomain(@Param("domain") String domain, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 已建立索引但为标识的页面
     *
     * @return
     */
    List<WebPage> getUnmarkedList();

    /**
     * 得到网页总数
     *
     * @return
     */
    Long getCount();

    /**
     * 更新网页
     *
     * @param webPage
     * @return
     */
    int update(WebPage webPage);

    /**
     * 通过id删除网页
     *
     * @param id
     * @return
     */
    int deleteById(long id);
}
