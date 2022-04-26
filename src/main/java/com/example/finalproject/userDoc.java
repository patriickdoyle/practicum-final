package com.example.finalproject;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class userDoc {
    private String userId;
    private long createdDate;

    public userDoc() {

    }
    public userDoc(String userId, long createdDate) {
        this.userId = userId;
        this.createdDate = createdDate;
    }

    @DynamoDbPartitionKey
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}
