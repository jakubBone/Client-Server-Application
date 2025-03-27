package com.jakub.bone.application;

import com.jakub.bone.domain.Mail;
import com.jakub.bone.utils.ConfigLoader;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticSearchService {
    // Client for communicating with Elasticsearch
    private final RestHighLevelClient client;
    private final String host = ConfigLoader.get("elastic.host");
    private final int port = Integer.parseInt(ConfigLoader.get("elastic.port"));

    // "elasticsearch" is Docker Compose service name
    public ElasticSearchService() throws IOException {
        this.client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
        );
        ensureIndexExists("mails");
    }

    // Checks if the specified index exists
    // If not, creates it with defined settings and mappings
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
            indicesClient.create(createIndexRequest, RequestOptions.DEFAULT);
        }
    }

    // Create index for Mail object into Elasticsearch
    public void indexMail(Mail mail) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("sender", mail.getSender().getUsername());
        jsonMap.put("recipient", mail.getRecipient().getUsername());
        jsonMap.put("message", mail.getMessage());
        jsonMap.put("sendTime", mail.getSendTime().toString());
        // Create index request for 'mails' index using  JSON map
        // Execute indexing operation
        IndexRequest indexRequest = new IndexRequest("mails").source(jsonMap);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    // Delete a document from the "mails" index based on id
    public void deleteMail(String mailId) throws IOException {
        // Create delete request for mail id
        // Execute deleting operation
        DeleteRequest deleteRequest = new DeleteRequest("mails", mailId);
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    // Searches for mails based on message content
    public SearchResponse searchMails(String queryText) throws IOException {
        SearchRequest searchRequest = new SearchRequest("mails");
        // Build search query using match query on "message" field
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("message", queryText));
        // Set the source of  search request
        searchRequest.source(sourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}
