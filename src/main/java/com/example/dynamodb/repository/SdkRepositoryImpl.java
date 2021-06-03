package com.example.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.example.dynamodb.entity.Animes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class SdkRepositoryImpl implements SdkRepository {
    ExecutorService service = Executors.newFixedThreadPool(10);
    private final DynamoDBMapper mapper;

    @Override
    public Animes save(Animes animes) {
        IntStream.range(0, 100).forEach(iterar ->
                service.execute(() -> this.saveThreads(animes))
        );

        return animes;
    }

    private Animes saveThreads(Animes animes) {
        System.out.println("Executando thread " + Thread.currentThread());
        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
        animes.setNote(ThreadLocalRandom.current().nextInt(0, 100));
        transactionWriteRequest.addUpdate(animes);
        mapper.transactionWrite(transactionWriteRequest);

        return animes;
    }
}
