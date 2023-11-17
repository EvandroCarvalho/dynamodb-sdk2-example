package com.example.dynamodb.repository;

import com.example.dynamodb.entity.Client;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ClientRepository {

    private final DynamoDbEnhancedClient dynamoDbClient;
    private DynamoDbTable<Client> clientDynamoDbTable;
    private DynamoDbIndex<Client> clientDynamoDbIndex;


    @PostConstruct
    public void setUp() {
        clientDynamoDbTable = dynamoDbClient.table("clients", TableSchema.fromBean(Client.class));
        clientDynamoDbIndex = dynamoDbClient.table("clients", TableSchema.fromBean(Client.class)).index("client_name-index");
    }

    public Client save(Client client) {
        clientDynamoDbTable.putItem(client);
        return client;
    }

    public List<Client> findAll() {
        PageIterable<Client> scan = clientDynamoDbTable.scan();
        return scan
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Client> findByNameIndex(String name) {

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(name)
                        .build());

        QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder()
                .scanIndexForward(true)
                .queryConditional(queryConditional)
                .limit(100)
                .build();

        List<Client> items = clientDynamoDbIndex.query(queryEnhancedRequest)
                .iterator()
                .next()
                .items();
        return items;
    }

    public List<Client> findByNameWithoutIndex(String value) {
        Expression expression = Expression.builder()
                .expression("client_name = :val1")
                .putExpressionValue(":val1", AttributeValue.builder().s(value).build())
                .build();

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .filterExpression(expression)
                .limit(100)
                .build();
        List<Client> collect = clientDynamoDbTable.scan(request).items().stream().collect(Collectors.toList());
        return collect;
    }

    public Client findByIdParition(String id) {
        return clientDynamoDbTable.getItem(Key.builder().partitionValue(id).build());
    }
}
