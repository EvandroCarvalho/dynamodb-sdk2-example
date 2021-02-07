package com.example.dynamodb.model;

import com.example.dynamodb.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private String id;
    private String email;
    private String clientName;
    private Address address;
}
