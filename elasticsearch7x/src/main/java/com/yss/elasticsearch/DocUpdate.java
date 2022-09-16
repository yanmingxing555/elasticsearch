package com.yss.elasticsearch;

import com.yss.elasticsearch.pack.ElasticsearchConnect;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * @author: ymx
 * @date: 2022/9/15
 * @description:
 */
public class DocUpdate {
    public static void main(String[] args) {
        ElasticsearchConnect.connect(client -> {
            //修改文档-请求对象
            UpdateRequest request = new UpdateRequest();
            //配置修改参数
            request.index("user").id("1001");

            //设置请求体，对数据进行修改
            request.doc(XContentType.JSON,"sex","女");
            //客户端发送请求，获取响应对象
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            System.out.println("_index:" + response.getIndex());
            System.out.println("_id:" + response.getId());
            System.out.println("_result:" + response.getResult());
        });
    }
}
