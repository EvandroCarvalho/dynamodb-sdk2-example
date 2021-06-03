package com.example.dynamodb.config;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.PredefinedBackoffStrategies;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.retry.RetryMode;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.retry.v2.RetryCondition;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.dynamodb.repository.AnimesRepository;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@EnableDynamoDBRepositories(basePackageClasses = AnimesRepository.class)
public class DynamodbConfig {

    @Value("${aws.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.credentials.secretKey}")
    private String secretKey;

    /**
     * Settings for timeouts
     *
     * Ref:
     * https://github.com/aws/aws-sdk-java/blob/master/aws-java-sdk-core/src/main/java/com/amazonaws/ClientConfiguration.java
     *
     * DEFAULT_CONNECTION_TIMEOUT - 10 s
     * DEFAULT_CLIENT_EXECUTION_TIMEOUT - 0 i.e., disabled
     * DEFAULT_REQUEST_TIMEOUT - 0 i.e., disabled
     * DEFAULT_SOCKET_TIMEOUT - 50 s
     */

    public static int connectionTimeout = 1000; // 1 s
    public static int clientExecutionTimeout = 5000; // 5 s
    public static int requestTimeout = 500; // 500 ms
    public static int socketTimeout = 1000; // 1 s

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));

//        PredefinedBackoffStrategies.ExponentialBackoffStrategy exponentialBackoffStrategy = new PredefinedBackoffStrategies
//                .ExponentialBackoffStrategy(100, 1000);

        PredefinedBackoffStrategies.FullJitterBackoffStrategy fullJitterBackoffStrategy = new PredefinedBackoffStrategies
                .FullJitterBackoffStrategy(100, 1000);

        ClientConfiguration clientConfiguration = new ClientConfiguration()
//                .withConnectionTimeout(connectionTimeout)
//                .withClientExecutionTimeout(clientExecutionTimeout)
//                .withRequestTimeout(requestTimeout)
//                .withSocketTimeout(socketTimeout)
                .withRetryPolicy(
                        new RetryPolicy((amazonWebServiceRequest, e, i) -> true,
                                fullJitterBackoffStrategy, 10, true));

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
//                .withClientConfiguration(clientConfiguration)
                .withRegion(Regions.US_EAST_2)
                .build();

        amazonDynamoDB.listTables();

        return amazonDynamoDB;
    }

    @Primary
    @Bean
    public DynamoDBMapper dynamoDBMapperCustom() {
        return new DynamoDBMapper(amazonDynamoDB());
    }
}
