//package com.example.dynamodb.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
//import software.amazon.awssdk.http.nio.netty.ProxyConfiguration;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
//
//@Configuration
//@RequiredArgsConstructor
//public class DynamodbAsyncClientConfig {
//
//    private CredentialsConfig credentialsConfig;
//
//    @Bean
//    public DynamoDbAsyncClient dynamodbAsyncClient() {
//
//        ProxyConfiguration nettyProxy = ProxyConfiguration.builder()
//                .host("proxy.com")
//                .port(8080)
//                .build();
//
//        NettyNioAsyncHttpClient.Builder httpClient = NettyNioAsyncHttpClient.builder();
//        httpClient.proxyConfiguration(nettyProxy);
//
//        return DynamoDbAsyncClient.builder()
//                .region(Region.US_EAST_2)
//                .credentialsProvider(() -> AwsBasicCredentials.create(credentialsConfig.getAccessKey(),
//                        credentialsConfig.getSecretKey()))
//                .httpClientBuilder(httpClient)
//                .build();
//    }
//}
