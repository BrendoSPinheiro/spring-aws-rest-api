package br.com.brendosp.springawsrestapi.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AppConfig {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Value("${aws.endpoint:http://localhost:4566}")
    private String awsEndpoint;

    @Bean
    @Profile("local")
    public DynamoDbClient localDynamoDbClient() {
        return DynamoDbClient.builder()
            .region(Region.of(awsRegion))
            .endpointOverride(URI.create(awsEndpoint))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

    @Bean
    @Profile("!local")
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }
}
