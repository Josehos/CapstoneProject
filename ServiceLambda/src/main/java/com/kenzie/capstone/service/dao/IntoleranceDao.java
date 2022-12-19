package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.IntoleranceData;
import com.kenzie.capstone.service.model.IntoleranceRecord;

import java.util.List;

public class IntoleranceDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public IntoleranceDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public IntoleranceData storeIntoleranceData(IntoleranceData intoleranceData) {
        try {
            mapper.save(intoleranceData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return intoleranceData;
    }

    public List<IntoleranceRecord> getIntoleranceData(String id) {
        IntoleranceRecord intoleranceRecord = new IntoleranceRecord();
        intoleranceRecord.setId(id);

        DynamoDBQueryExpression<IntoleranceRecord> queryExpression = new DynamoDBQueryExpression<IntoleranceRecord>()
                .withHashKeyValues(intoleranceRecord)
                .withConsistentRead(false);

        return mapper.query(IntoleranceRecord.class, queryExpression);
    }

    public IntoleranceRecord setIntoleranceData(String id, String data) {
        IntoleranceRecord intoleranceRecord = new IntoleranceRecord();
        intoleranceRecord.setId(id);
        intoleranceRecord.setData(data);

        try {
            mapper.save(intoleranceRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return intoleranceRecord;
    }
}
