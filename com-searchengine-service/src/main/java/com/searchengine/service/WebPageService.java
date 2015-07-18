package com.searchengine.service;

import com.searchengine.domain.WebPage;
import com.searchengine.domain.Website;
import com.searchengine.domain.vo.WebPageVO;
import com.sun.istack.internal.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WebPageService {
    /**
     * 添加新的网页索引
     *
     * @param webPage
     * @return
     */
    long add(WebPage webPage);

    /**
     * 通过id得到网页
     *
     * @param id
     * @return
     */
    WebPage getById(long id);

    /**
     * 得到所有已经访问的网页
     *
     * @return
     */
    List<WebPage> getList(@Nullable Boolean analyzed);

    /**
     * 分页得到网站
     *
     * @param domain
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<Website> getWebsiteList(String domain, int pageIndex, int pageSize);

    /**
     * 得到网站总数
     *
     * @return
     */
    Long getWebsiteCount();

    /**
     * 通过id列表得到网页
     *
     * @return
     */
    List<WebPageVO> getListByIdList(List<Long> idList);

    /**
     * 得到所有网页
     *
     * @return
     */
    List<WebPage> getWebPageList(@Nullable Boolean analyzed);

    /**
     * 分页得到网页
     *
     * @param pageSize
     * @param pageSize
     * @return
     */
    List<WebPage> getListBy(int pageIndex, int pageSize);

    /**
     * 通过域名分页得到网页
     *
     * @param domain
     * @param pageSize
     * @param pageSize
     * @return
     */
    List<WebPage> getListByDomain(String domain, int pageIndex, int pageSize);

    /**
     * 标记已建立索引但为标识的页面
     *
     * @return
     */
    void markWebPage();

    /**
     * 修改网页信息
     *
     * @return
     */
    int update(WebPage webPage);

    /**
     * 得到网页总数
     *
     * @return
     */
    Long getCount();
}
