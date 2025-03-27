package com.jakub.bone.application;

import com.jakub.bone.domain.Mail;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticSearchService {
    private RestHighLevelClient client;

    public ElasticSearchService() throws IOException {
        this.client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("elasticsearch", 9200, "http"))
        );
        ensureIndexExists("mails");
    }


    private void ensureIndexExists(String indexName) throws IOException {
        IndicesClient indicesClient = client.indices();
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = indicesClient.exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!exists) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings("{\n" +
                    "  \"number_of_shards\": 1,\n" +
                    "  \"number_of_replicas\": 0\n" +
                    "}", XContentType.JSON);
            createIndexRequest.mapping("{\n" +
                    "  \"properties\": {\n" +
                    "    \"sender\": { \"type\": \"keyword\" },\n" +
                    "    \"recipient\": { \"type\": \"keyword\" },\n" +
                    "    \"message\": { \"type\": \"text\" },\n" +
                    "    \"sendTime\": { \"type\": \"date\" }\n" +
                    "  }\n" +
                    "}", XContentType.JSON);
            CreateIndexResponse createIndexResponse = indicesClient.create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println("Utworzono indeks " + indexName + ": " + createIndexResponse.isAcknowledged());
        }
    }

    // Indeksowanie wiadomości
    public void indexMail(Mail mail) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("sender", mail.getSender().getUsername());
        jsonMap.put("recipient", mail.getRecipient().getUsername());
        jsonMap.put("message", mail.getMessage());
        jsonMap.put("sendTime", mail.getSendTime().toString());
        // Jeżeli masz identyfikator wiadomości, możesz go wykorzystać jako ID
        IndexRequest indexRequest = new IndexRequest("mails").source(jsonMap);
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("Indexed mail with id: " + response.getId());
    }

    // Usuwanie wiadomości z indeksu
    public void deleteMail(String mailId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("mails", mailId);
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("Deleted mail with id: " + response.getId());
    }

    // Wyszukiwanie wiadomości na podstawie treści
    public SearchResponse searchMails(String queryText) throws IOException {
        SearchRequest searchRequest = new SearchRequest("mails");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("message", queryText));
        searchRequest.source(sourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    public void close() throws IOException {
        client.close();
    }
}
