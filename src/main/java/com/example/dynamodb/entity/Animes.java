package com.example.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "animes")
public class Animes {

    @DynamoDBHashKey(attributeName = "id_animes")
    private String id;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "name-index")
    private String name;

    private String author;

    private int note;

    private Long version;

    public String getId() {
        return id;
    }

    @DynamoDbAttribute("animes_names")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("author")
    public String getAuthor() {
        return author;
    }

    @DynamoDbAttribute("note")
    public int getNote() {
        return note;
    }

    public Long getVersion() {
        return version;
    }
}
