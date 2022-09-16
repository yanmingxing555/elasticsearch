package com.yss.elasticsearch.pack;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author: ymx
 * @date: 2022/9/15
 * @description:
 */
public interface ElasticsearchTask {

    void doSomething(RestHighLevelClient client) throws Exception;

}
