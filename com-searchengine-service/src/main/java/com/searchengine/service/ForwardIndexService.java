package com.searchengine.service;

import com.searchengine.domain.ForwardIndex;
import com.searchengine.domain.WordDF;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ForwardIndexService {
    /**
     * 添加新的网页索引
     * @param forwardIndex
     * @return
     */
    long add(ForwardIndex forwardIndex);

    ForwardIndex getById(long id);

    /**
     * 得到所有网页索引
     * @return
     */
    List<ForwardIndex> getList(int pageIndex,int pageSize);

    /**
     * 得到document frequency
     * @return
     */
    List<WordDF> getWordDFList();

    /**
     * 得到网页索引
     * @return
     */
    List<ForwardIndex> getListByWordIdList(List<Long> wordIdList);

    /**
     * 得到已经建立了索引的网页
     * @return
     */
    List<Long> getDistinctPidList();

    /**
     * 修改正排索引
     * @param forwardIndex
     * @return
     */
    int update(ForwardIndex forwardIndex);
}
