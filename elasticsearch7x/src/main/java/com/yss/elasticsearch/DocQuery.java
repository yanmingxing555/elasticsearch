package com.yss.elasticsearch;

import com.yss.elasticsearch.pack.ElasticsearchConnect;
import com.yss.elasticsearch.pack.ElasticsearchTask;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Map;

/**
 * @author: ymx
 * @date: 2022/9/15
 * @description:
 *  查询所有 & 条件查询 & 分页查询 & 查询排序
 *  组合查询 & 范围查询
 *  模糊查询 & 高亮查询
 *  最大值查询 & 分组查询
 */
public class DocQuery {
    public static void main(String[] args) {
        ElasticsearchConnect.connect(SEARCH_ALL);//查询所有
        // ElasticsearchConnect.connect(SEARCH_BY_CONDITION);//条件查询
        // ElasticsearchConnect.connect(SEARCH_BY_PAGING);//分页查询
        // ElasticsearchConnect.connect(SEARCH_WITH_ORDER);//查询排序
        // ElasticsearchConnect.connect(SEARCH_BY_BOOL_CONDITION);//组合查询
        // ElasticsearchConnect.connect(SEARCH_BY_RANGE);//范围查询
        // ElasticsearchConnect.connect(SEARCH_BY_FUZZY_CONDITION);//模糊查询
        // ElasticsearchConnect.connect(SEARCH_WITH_HIGHLIGHT);//高亮查询
        // ElasticsearchConnect.connect(SEARCH_WITH_MAX);//最大值查询
        // ElasticsearchConnect.connect(SEARCH_WITH_GROUP);//分组查询
    }
    //查询所有
    public static final ElasticsearchTask SEARCH_ALL = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //查询所有数据
        sourceBuilder.query(QueryBuilders.matchAllQuery());


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    };
    //条件查询
    public static final ElasticsearchTask SEARCH_BY_CONDITION = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //2.条件查询
        sourceBuilder.query(QueryBuilders.termQuery("age", "30"));


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    };
    //分页查询
    public static final ElasticsearchTask SEARCH_BY_PAGING = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //3.分页查询
        sourceBuilder.from(0);//当前页其实索引(第一条数据的顺序号)， from
        sourceBuilder.size(2);//每页显示多少条 size


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    };
    //排序查询
    public static final ElasticsearchTask SEARCH_WITH_ORDER = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //4.排序
        sourceBuilder.sort("age", SortOrder.ASC);


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    };
    //组合查询
    public static final ElasticsearchTask SEARCH_BY_BOOL_CONDITION = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //组合查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 必须包含
        boolQueryBuilder.must(QueryBuilders.matchQuery("age", "30"));
        // 一定不含
        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("name", "zhangsan"));
        // 可能包含
        boolQueryBuilder.should(QueryBuilders.matchQuery("sex", "男"));
        sourceBuilder.query(boolQueryBuilder);


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    };
    //范围查询
    public static final ElasticsearchTask SEARCH_BY_RANGE = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /***************范围查询**************/
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        rangeQuery.gte("30");// 大于等于
        rangeQuery.lte("40");// 小于等于
        sourceBuilder.query(rangeQuery);

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    };
    //模糊查询
    public static final ElasticsearchTask SEARCH_BY_FUZZY_CONDITION = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //模糊查询
        sourceBuilder.query(QueryBuilders.fuzzyQuery("name","wangwu").fuzziness(Fuzziness.ONE));

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    };
    //高亮查询
    public static final ElasticsearchTask SEARCH_WITH_HIGHLIGHT = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //构建查询方式：高亮查询
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("name","zhangsan");
        //设置查询方式
        sourceBuilder.query(termsQueryBuilder);
        //构建高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");//设置标签前缀
        highlightBuilder.postTags("</font>");//设置标签后缀
        highlightBuilder.field("name");//设置高亮字段
        //设置高亮构建对象
        sourceBuilder.highlighter(highlightBuilder);


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits::::>>");
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            //打印高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            System.out.println(highlightFields);
        }
        System.out.println("<<::::");
    };
    //最大值查询
    public static final ElasticsearchTask SEARCH_WITH_MAX = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //构建查询方式：最大值查询
        sourceBuilder.aggregation(AggregationBuilders.max("maxAge").field("age"));


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println(response);
    };
    //分组查询
    public static final ElasticsearchTask SEARCH_WITH_GROUP = client -> {
        //创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //构建查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //构建查询方式：分组查询
        sourceBuilder.aggregation(AggregationBuilders.terms("age_groupby").field("age"));


        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println(response);
    };
}
