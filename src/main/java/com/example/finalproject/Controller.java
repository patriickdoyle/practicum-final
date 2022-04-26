/*package com.example.finalproject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class Controller {

    @PostMapping("/users")
    public Map<String, Object> createUser() {
        UUID uuid = UUID.randomUUID(); //uuid randomizes username
        long seconds = System.currentTimeMillis(); //timestamp
        Map<String, Object> json = new HashMap<>(); //map holds timestamp and userID

        json.put("createdDate", seconds);
        json.put("userId", uuid.toString());



        return json;
    }
}*/
