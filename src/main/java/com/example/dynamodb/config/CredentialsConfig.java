package com.example.dynamodb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws.credentials")
public class CredentialsConfig {
    private String accessKey;
    private String secretKey;
}
