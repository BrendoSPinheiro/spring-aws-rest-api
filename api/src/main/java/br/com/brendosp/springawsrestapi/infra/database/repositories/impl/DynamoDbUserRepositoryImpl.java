package br.com.brendosp.springawsrestapi.infra.database.repositories.impl;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.infra.database.repositories.IUserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@Primary
@Repository
@RequiredArgsConstructor
public class DynamoDbUserRepositoryImpl implements IUserRepository {

    private static final String TABLE_NAME = "users";

    private final DynamoDbClient dynamoDbClient;

    @Override
    public Optional<User> findById(UUID id) {
        var request = GetItemRequest.builder()
            .tableName(TABLE_NAME)
            .key(Map.of("id", AttributeValue.builder().s(id.toString()).build()))
            .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        if (!response.hasItem()) {
            return Optional.empty();
        }

        var user = mapToUser(response.item());

        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var request = QueryRequest.builder()
            .tableName(TABLE_NAME)
            .indexName("email-index")
            .expressionAttributeValues(Map.of(":email", AttributeValue.builder().s(email).build()))
            .keyConditionExpression("email = :email")
            .build();

        QueryResponse response = dynamoDbClient.query(request);

        if (response.count().equals(0)) {
            return Optional.empty();
        }

        var user = mapToUser(response.items().getFirst());

        return Optional.of(user);
    }

    @Override
    public User save(User user) {
        var record = Map.of(
            "id", AttributeValue.builder().s(user.getId().toString()).build(),
            "name", AttributeValue.builder().s(user.getName()).build(),
            "email", AttributeValue.builder().s(user.getEmail()).build(),
            "password", AttributeValue.builder().s(user.getPassword()).build(),
            "created_at", AttributeValue.builder().s(user.getCreatedAt().toString()).build(),
            "updated_at", AttributeValue.builder().s(user.getUpdatedAt().toString()).build()
        );

        var request = PutItemRequest.builder()
            .tableName(TABLE_NAME)
            .item(record)
            .conditionExpression("attribute_not_exists(email)")
            .build();

        dynamoDbClient.putItem(request);

        return user;
    }

    @Override
    public void deleteById(UUID id) {
        var request = DeleteItemRequest.builder()
            .tableName(TABLE_NAME)
            .key(Map.of("id", AttributeValue.builder().s(id.toString()).build()))
            .build();

        dynamoDbClient.deleteItem(request);
    }

    private User mapToUser(Map<String, AttributeValue> item) {
        var user = new User();
        user.setId(UUID.fromString(item.get("id").s()));
        user.setName(item.get("name").s());
        user.setEmail(item.get("email").s());
        user.setPassword(item.get("password").s());
        user.setCreatedAt(LocalDateTime.parse(item.get("created_at").s()));
        user.setUpdatedAt(LocalDateTime.parse(item.get("updated_at").s()));
        return user;
    }
}
