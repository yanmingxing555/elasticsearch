package com.yss.elasticsearch;

import com.yss.elasticsearch.pack.ElasticsearchConnect;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
/**
 * @author: ymx
 * @date: 2022/9/15
 * @description: 文档删除
 */
public class Doc_Delete {
    public static void main(String[] args) {
        ElasticsearchConnect.connect(client -> {
            //创建请求对象
            DeleteRequest request = new DeleteRequest().index("user").id("1001");
            //客户端发送请求，获取响应对象
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            //打印信息
            System.out.println(response.toString());
        });
    }
}
