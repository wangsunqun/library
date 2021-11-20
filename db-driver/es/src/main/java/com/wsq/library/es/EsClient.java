package com.wsq.library.es;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wsq
 * 2021/5/12 21:02
 */
@Slf4j
public class EsClient {
    public static RestHighLevelClient esClient;

    private static String clusterAddress = "kiibos.com:9200";

    private static final List<HttpHost> hostList = new ArrayList<>();

    static {
        String[] ipPorts = clusterAddress.split(",");
        for (String ipPort : ipPorts) {
            String[] httpHost = ipPort.split(":", 2);
            hostList.add(new HttpHost(httpHost[0], Integer.parseInt(httpHost[1]), "http"));
        }

        esClient = new RestHighLevelClient(RestClient.builder(hostList.toArray(new HttpHost[0])));
    }

    //============ 删除 ============//
    public static int delete(String index, String id) {
        if (null == index) {
            throw new RuntimeException("index is null");
        }

        DeleteRequest request = new DeleteRequest(index, id);

        try {
            DeleteResponse delete = esClient.delete(request, RequestOptions.DEFAULT);
            return delete.getShardInfo().getSuccessful();
        } catch (IOException e) {
            log.error("es method:delete, index:{}, id:{}", index, id);
            throw new RuntimeException("es delete error", e);
        }
    }

    //============ 单条新增，覆盖文档 ============//
    public static <T> void save(String index, T data) {
        save(index, null, data);
    }

    public static <T> void save(String index, String id, T data) {
        save(index, id, data, WriteRequest.RefreshPolicy.IMMEDIATE);
    }

    public static <T> void save(String index, String id, T data, WriteRequest.RefreshPolicy refreshPolicy) {
        if (null == index) {
            throw new RuntimeException("index is null");
        }

        IndexRequest request = new IndexRequest(index).
                id(id).
                setRefreshPolicy(refreshPolicy).
                source(JSON.toJSONString(data), XContentType.JSON);

        try {
            esClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es method:save, index:{}, id:{}, data:{}", index, id, data);
            throw new RuntimeException("es save error", e);
        }
    }

    //============ 批量新增，覆盖文档 ============//
    public static <T> void saveBatch(String index, Map<T, String> data) {
        saveBatch(index, data, WriteRequest.RefreshPolicy.IMMEDIATE);
    }

    public static <T> void saveBatch(String index, Map<T, String> data, WriteRequest.RefreshPolicy refreshPolicy) {
        if (null == index) {
            throw new RuntimeException("index is null");
        }

        if (MapUtils.isEmpty(data)) return;

        BulkRequest request = new BulkRequest();
        for (Map.Entry<T, String> entry : data.entrySet()) {
            request.add(new IndexRequest(index).
                    id(entry.getValue()).
                    source(JSON.toJSONString(entry.getValue()), XContentType.JSON));
        }

        try {
            esClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es method:saveBatch, index:{}, data:{}", index, data);
            throw new RuntimeException("es saveBatch error", e);
        }
    }

    //============ 单条更新文档 ============//
    public static <T> void update() {

    }
}