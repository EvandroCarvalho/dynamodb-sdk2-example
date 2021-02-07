package com.example.dynamodb.config;

import com.example.dynamodb.entity.Animes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamodbConfig {

    @Value("${aws.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.credentials.secretKey}")
    private String secretKey;

    @Bean
    public DynamoDbClient dynamoDbClient() {
//        ProxyConfiguration.Builder proxyConfig = ProxyConfiguration.builder();
//        proxyConfig.endpoint(URI.create("proxy.com"));

        ApacheHttpClient.Builder apacheHttpClient = ApacheHttpClient.builder();
//                .proxyConfiguration(proxyConfig.build());

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .httpClientBuilder(apacheHttpClient)
                .region(Region.US_EAST_2)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();


//        dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("animes").build());

        //Update item using DynamodbTable
        DynamoDbTable<Animes> dynamoDbTable = enhancedClient.table("animes", TableSchema.fromClass(Animes.class));
        UpdateItemEnhancedRequest<Animes> update = UpdateItemEnhancedRequest.builder(Animes.class)
                .ignoreNulls(true)
                .item(Animes.builder().id("1").build())
                .build();
//        dynamoDbTable.updateItem(update);


        // Update Item using dynamodbClient
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName("animes")
                .key(Map.of("id_animes", AttributeValue.builder().s("2").build()))
                .updateExpression("set note = note + :val1")
                .expressionAttributeValues(Map.of(":val1", AttributeValue.builder().n("2").build()))
                .returnValues("UPDATED_NEW") // [ALL_NEW, UPDATED_OLD, ALL_OLD, NONE, UPDATED_NEW]
                .build();

        dynamoDbClient.updateItem(updateItemRequest);

        GetItemRequest itemRequest = GetItemRequest.builder()
                .tableName("animes")
                .key(Map.of("id_animes", AttributeValue.builder().s("1").build()))
                .attributesToGet("animes_names")
                .build();

        dynamoDbClient.getItem(itemRequest);

        return dynamoDbClient;
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient())
                .build();
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(Region.US_EAST_2)
                .build();
    }

}
