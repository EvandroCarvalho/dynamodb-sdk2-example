package com.example.dynamodb.repository;

import com.example.dynamodb.entity.Client;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.internal.client.DefaultDynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import javax.annotation.PostConstruct;
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
    private final Timer saveTimer;
    private final Timer consultScanTimer;
    private final Timer consultByIndex;
    private final Timer consultWithoutIndex;


    @PostConstruct
    public void setUp() {
        clientDynamoDbTable = dynamoDbClient.table("clients", TableSchema.fromBean(Client.class));
        clientDynamoDbIndex = dynamoDbClient.table("clients", TableSchema.fromBean(Client.class)).index("client_name-index");
    }

    public Client save(Client client) {
        LocalDateTime now = LocalDateTime.now();
        clientDynamoDbTable.putItem(client);
        saveTimer.record(Duration.between(now, LocalDateTime.now()));
        return client;
    }

    public List<Client> findAll() {
        LocalDateTime now = LocalDateTime.now();
        PageIterable<Client> scan = clientDynamoDbTable.scan();
        consultScanTimer.record(Duration.between(now, LocalDateTime.now()));
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

        LocalDateTime now = LocalDateTime.now();
        List<Client> items = clientDynamoDbIndex.query(queryEnhancedRequest)
                .iterator()
                .next()
                .items();
        consultByIndex.record(Duration.between(now, LocalDateTime.now()));
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
        LocalDateTime now = LocalDateTime.now();
        List<Client> collect = clientDynamoDbTable.scan(request).items().stream().collect(Collectors.toList());
        consultWithoutIndex.record(Duration.between(now, LocalDateTime.now()));
        return collect;
    }

    public Client findByIdParition(String id) {
        return clientDynamoDbTable.getItem(Key.builder().partitionValue(id).build());
    }
}
