package com.searchengine.service;

import com.searchengine.domain.PageWord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PageWordService {
    /**
     * 得到正排索引视图列表：通过词id
     *
     * @param wordIdList
     * @return
     */
    List<PageWord> getListByWordIdList(List<Long> wordIdList);

    /**
     * 分页得到索引数据
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<PageWord> getListBy(int pageIndex, int pageSize);

    /**
     * 得到索引总数
     *
     * @return
     */
    Long getCount();
}
