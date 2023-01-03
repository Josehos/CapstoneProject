package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.IngredientsData;
import com.kenzie.capstone.service.model.IngredientsRecord;

import java.util.List;

public class NonCachingIngredientsDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public NonCachingIngredientsDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public IngredientsData storeIngredientsData(IngredientsData ingredientsData) {
        try {
            mapper.save(ingredientsData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return ingredientsData;
    }

    public List<IngredientsRecord> getIngredientsData(String id) {
        IngredientsRecord ingredientsRecord = new IngredientsRecord();
        ingredientsRecord.setId(id);

        DynamoDBQueryExpression<IngredientsRecord> queryExpression = new DynamoDBQueryExpression<IngredientsRecord>()
                .withHashKeyValues(ingredientsRecord)
                .withConsistentRead(false);

        return mapper.query(IngredientsRecord.class, queryExpression);
    }

    public IngredientsRecord setIngredientsData(String id, String data) {
        IngredientsRecord ingredientsRecord = new IngredientsRecord();
        ingredientsRecord.setId(id);
        ingredientsRecord.setData(data);

        try {
            mapper.save(ingredientsRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return ingredientsRecord;
    }
}
