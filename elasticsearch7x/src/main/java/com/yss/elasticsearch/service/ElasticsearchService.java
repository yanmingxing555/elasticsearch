package com.yss.elasticsearch.service;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Elasticsearch工具类
 */
public class ElasticsearchService {

    public String host = "localhost";
    public int port = 9200;
    public String scheme = "http";
    public RestHighLevelClient client = null;

    @PostConstruct
    public void init() {
        try {
            if (client != null) {
                client.close();
            }
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, scheme)));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * 创建索引
     * @param indexName 索引名称
     * @param indexJson 映射规则
     * @param shardNo 分片数量
     * @param replicas 副本数量
     * @return
     */
    public boolean createIndex(String indexName, String indexJson, int shardNo, int replicas) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder().put("index.number_of_shards", shardNo).put("index.number_of_replicas", replicas));
        request.mapping(indexJson, XContentType.JSON);
        CreateIndexResponse res = null;
        try {
            res = client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res ==null || !res.isAcknowledged()) {
            return false;
        }
        return true;
    }

    /**
     * Description: 判断某个index是否存在
     * @param index index名
     * @return boolean
     */
    public boolean indexExist(String index) {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Description: 插入数据
     * @param index index
     * @param json  带插入列表
     */
    public void insertJson(String index, String id, String json) {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest(index).id(id).source(json,XContentType.JSON));
        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Description: 批量插入数据
     * @param index
     * @param jsonMap
     */
    public void insertJsons(String index, Map<String, String> jsonMap) {
        BulkRequest request = new BulkRequest();
        for(Map.Entry<String,String> entry : jsonMap.entrySet()){
            request.add(new IndexRequest(index).id(entry.getKey()).source(entry.getValue(), XContentType.JSON));
        }
        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 搜索
     * @param index   index
     * @param builder 查询参数
     * @return java.util.ArrayList
     */
    public List<String> search(String index, SearchSourceBuilder builder) {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<String> res = new ArrayList<String>(hits.length);
            for (SearchHit hit : hits) {
                res.add(hit.getSourceAsString());
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 搜索
     *
     * @param index   index
     * @return String
     */
    public String searchById(String index, String id) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(new TermQueryBuilder("_id", id));
        List<String> res = search(index, builder);
        if (res.size() > 0) {
            return res.get(0);
        } else {
            return null;
        }
    }

    /**
     * Description: 删除index
     *
     * @param index index
     * @return void
     */
    public void deleteIndex(String index) {
        try {
            client.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
