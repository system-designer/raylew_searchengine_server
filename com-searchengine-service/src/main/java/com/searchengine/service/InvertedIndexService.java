package com.searchengine.service;

import com.searchengine.domain.InvertedIndex;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvertedIndexService {
    /**
     * 通过wordid得到所有倒排索引
     *
     * @return
     */
    List<InvertedIndex> getList(Long wid);
}
