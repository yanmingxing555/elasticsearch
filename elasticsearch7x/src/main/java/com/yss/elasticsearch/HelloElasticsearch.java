package com.yss.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author: ymx
 * @date: 2022/9/15
 * @description:
 */
public class HelloElasticsearch {
    public static void main(String[] args) throws IOException {
        //创建客户端对象
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost",9200,"http")));
        System.out.println(client);
        //关闭客户端连接
        client.close();
    }
}
