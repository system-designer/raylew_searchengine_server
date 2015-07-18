package com.searchengine.service.test;

import com.searchengine.domain.WebPage;
import com.searchengine.service.WebPageService;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试网页模块
 */
@ContextConfiguration({"classpath:/spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PageIndexServiceTest {
    /**
     * 网页服务
     */
    @Autowired
    WebPageService webPageService;

    /**
     * 创建网页
     * @return
     */
    public static WebPage createWebPage() {
        WebPage webPage = new WebPage();
        //设置网页信息
        webPage.setUrl("test.com");
        webPage.setTitle("Test");
        return webPage;
    }

    /**
     * 测试添加网页
     */
    @Test
    public void addTest() {
        WebPage webPage = createWebPage();
        long id = webPageService.add(webPage);
        Assert.assertTrue(id > 0);
    }

    @Test
    public void findByIdTest() {
        WebPage webPage = createWebPage();
        long id = webPageService.add(webPage);
        WebPage task = webPageService.getById(id);
        Assert.assertNotNull(task);
    }

    @Test
    public void getListTest() {
        List<WebPage> webPageList = webPageService.getListBy(1, 20);
        Assert.assertTrue(webPageList.size() > 0);
    }

    @Test
    public void getCountTest() {
        Long ret = webPageService.getCount();
        Assert.assertTrue(ret > 0);
    }
}
