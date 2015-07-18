package com.searchengine.utils;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * Created by liold on 2014/11/9.
 */
public class BeanUtils {
    /**
     *
     * @param sourceObject
     * @param destObjectClass
     * @return
     */
    public static <T> T copyProperties(Object sourceObject,Class<T> destObjectClass){
        Mapper mapper = new DozerBeanMapper();
        T destObject = mapper.map(sourceObject,destObjectClass);
        return destObject;
    }
}
