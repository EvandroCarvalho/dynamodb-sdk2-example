package com.example.dynamodb.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

//        modelMapper.addMappings( (Customer, Client) -> {
//            modelMapper.map();
//        })
//
//        modelMapper.createTypeMap(Client.class, Customer.class).setConverter(
//                mappingContext -> {
//                    Client client = mappingContext.getSource();
//                }
//        )
        return modelMapper;
    }

    public static void main(String[] args) {
        List<String> listString = new ArrayList<>(List.of("a", "b", "c", "d"));
        List<List<String>> internalList = new ArrayList<>();
        internalList.add(listString);
        internalList.add(listString);

        List<String> collect = internalList.stream()
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        System.out.println(collect);
    }

}
