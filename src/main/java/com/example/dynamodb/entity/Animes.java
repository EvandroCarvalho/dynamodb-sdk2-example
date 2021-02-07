package com.example.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@DynamoDbBean
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Animes {

    private String id;

    private String name;

    private String author;

    private int note;

    private Long version;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id_animes")
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("animes_names")
    public String getName() {
        return name;
    }

//    @DynamoDbSecondaryPartitionKey(indexNames = "author-index")
    @DynamoDbAttribute("author")
    public String getAuthor() {
        return author;
    }

    @DynamoDbAttribute("note")
    public int getNote() {
        return note;
    }

    @DynamoDbVersionAttribute
    public Long getVersion() {
        return version;
    }
}
