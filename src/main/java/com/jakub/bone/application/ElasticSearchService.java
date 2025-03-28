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

/**
 * The ElasticSearchService class provides an interface for interacting with Elasticsearch
 * An ES index is a structure used to organize and store documents, enabling fast search
 * Handles operations on the "mails" index such as creating, searching, and deleting documents
 */
public class ElasticSearchService {
    // Client for communicating with Elasticsearch
    private final RestHighLevelClient client;
    private final String host = ConfigLoader.get("elastic.host");
    private final int port = Integer.parseInt(ConfigLoader.get("elastic.port"));

    // Host "elasticsearch" is Docker Compose service name
    public ElasticSearchService() throws IOException {
        this.client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
        );
        ensureIndexExists("mails");
    }

    // Elasticsearch index is created using a CreateIndexRequest
    // It specify its name, settings, and mappings to define how documents are stored and search
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
        // Create index request for 'mails' and execute indexing operation
        IndexRequest indexRequest = new IndexRequest("mails").source(jsonMap);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    // Delete a document from the "mails" index based on id
    public void deleteMail(String mailId) throws IOException {
        // Create delete request for mail id and execute deleting operation
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
