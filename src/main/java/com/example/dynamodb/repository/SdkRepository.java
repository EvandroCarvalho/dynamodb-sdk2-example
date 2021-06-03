package com.example.dynamodb.repository;

import com.example.dynamodb.entity.Animes;

public interface SdkRepository {
    Animes save(Animes animes);
}
