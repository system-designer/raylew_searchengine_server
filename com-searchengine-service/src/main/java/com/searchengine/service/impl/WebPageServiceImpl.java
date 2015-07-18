package com.searchengine.service.impl;

import com.searchengine.dao.WebPageDao;
import com.searchengine.domain.WebPage;
import com.searchengine.domain.Website;
import com.searchengine.domain.vo.WebPageVO;
import com.searchengine.service.WebPageService;
import com.searchengine.utils.BeanUtils;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("webPageService")
public class WebPageServiceImpl extends ServiceBase implements WebPageService {

    @Autowired
    private WebPageDao webPageDao;

    @Override
    public long add(WebPage webPage) {
        long id = GenerateId();
        if (webPage.getId() == 0) {
            webPage.setId(id);
        }
        webPage.setAnalyzed(false);
        Date nowDate = new Date();
        webPage.setCreatedTime(nowDate);
        webPage.setLastUpdatedTime(nowDate);
        int ret = webPageDao.add(webPage);
        if (ret > 0) {
            return id;
        }
        return 0;
    }

    @Override
    public WebPage getById(long id) {
        WebPage webPage = webPageDao.getById(id);
        if (webPage == null) {
            return null;
        }
        return webPage;
    }

    @Override
    public List<WebPage> getList(@Nullable Boolean analyzed) {
        List<WebPage> webPageList = webPageDao.getList(analyzed);
        return webPageList;
    }

    @Override
    public List<WebPageVO> getListByIdList(List<Long> idList) {
        List<WebPage> webPageList = webPageDao.getListByIdList(idList);

        if (webPageList != null) {
            List<WebPageVO> webPageVOList = new ArrayList<WebPageVO>();
            for (WebPage webPage : webPageList) {
                WebPageVO webPageVO = BeanUtils.copyProperties(webPage, WebPageVO.class);
                webPageVOList.add(webPageVO);
            }

            return webPageVOList;
        }

        return null;
    }

    @Override
    public List<WebPage> getWebPageList(@Nullable Boolean analyzed) {
        List<WebPage> webPageList = webPageDao.getList(analyzed);
        return webPageList;
    }

    @Override
    public List<WebPage> getListBy(int pageIndex, int pageSize) {
        List<WebPage> webPageList = webPageDao.getListBy((pageIndex - 1) * pageSize, pageSize);
        return webPageList;
    }

    @Override
    public List<WebPage> getListByDomain(String domain, int pageIndex, int pageSize) {
        List<WebPage> webPageList = webPageDao.getListByDomain(domain, (pageIndex - 1) * pageSize, pageSize);
        return webPageList;
    }

    @Override
    public void markWebPage() {
        while (true) {
            List<WebPage> webPageList = webPageDao.getUnmarkedList();
            if (webPageList != null) {
                for (WebPage webPage : webPageList) {
                    webPage.setAnalyzed(true);
                    webPageDao.update(webPage);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public int update(WebPage webPage) {
        return webPageDao.update(webPage);
    }

    @Override
    public Long getCount() {
        return webPageDao.getCount();
    }

    @Override
    public List<Website> getWebsiteList(String domain, int pageIndex, int pageSize) {
        return webPageDao.getWebsiteList(domain, (pageIndex - 1) * pageSize, pageSize);
    }

    @Override
    public Long getWebsiteCount() {
        return webPageDao.getWebsiteCount();
    }
}
