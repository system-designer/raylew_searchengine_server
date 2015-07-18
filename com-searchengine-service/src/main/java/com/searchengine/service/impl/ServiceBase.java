package com.searchengine.service.impl;

import com.searchengine.utils.IdGeneratorWorker;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

/**
 * 所有服务的基类
 * User: xuxp
 * Date: 14-11-7
 * Time: 下午7:12
 * To change this template use File | Settings | File Templates.
 */
public class ServiceBase {

    protected final Logger logger = Logger.getLogger(this.getClass());

    @Resource
    protected IdGeneratorWorker idGeneratorWorker;

    protected long GenerateId()
    {
        return idGeneratorWorker.nextId();
    }
}
