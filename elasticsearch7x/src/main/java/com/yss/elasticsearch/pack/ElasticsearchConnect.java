package com.yss.elasticsearch.pack;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author: ymx
 * @date: 2022/9/22
 * @description:
 */
public class ElasticsearchConnect {

    public static void connect(ElasticsearchTask task){
        //创建客户端对象
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200,"http")));
        try {
            task.doSomething(client);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            //关闭客户端连接
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
