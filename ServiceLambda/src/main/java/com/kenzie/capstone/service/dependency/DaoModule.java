package com.kenzie.capstone.service.dependency;


import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.caching.CachingIngredientsDao;
import com.kenzie.capstone.service.caching.CachingIntoleranceDao;
import com.kenzie.capstone.service.dao.*;
import com.kenzie.capstone.service.util.DynamoDbClientProvider;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides DynamoDBMapper instance to DAO classes.
 */
@Module
public class DaoModule {

    @Singleton
    @Provides
    @Named("DynamoDBMapper")
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
    }

    @Singleton
    @Provides
    @Named("IngredientsDao")
    @Inject
    public IngredientsDao provideIngredientsDao(@Named("CacheClient") CacheClient cacheClient,
                                                @Named("NonCachingIngredientsDao") NonCachingIngredientsDao nonCachingIngredientsDao) {
        return new CachingIngredientsDao(cacheClient, nonCachingIngredientsDao);
    }

    @Singleton
    @Provides
    @Named("NonCachingIngredientsDao")
    @Inject
    public NonCachingIngredientsDao provideNonCachingIngredientsDao(@Named("DynamoDBMapper") DynamoDBMapper mapper) {
        return new NonCachingIngredientsDao(mapper);
    }

    @Singleton
    @Provides
    @Named("IntoleranceDao")
    @Inject
    public IntoleranceDao provideIntoleranceDao(@Named("CacheClient") CacheClient cacheClient,
                                                @Named("NonCachingIntoleranceDao") NonCachingIntoleranceDao nonCachingIntoleranceDao) {
        return new CachingIntoleranceDao(cacheClient, nonCachingIntoleranceDao);
    }

    @Singleton
    @Provides
    @Named("NonCachingIntoleranceDao")
    @Inject
    public NonCachingIntoleranceDao provideNonCachingIntoleranceDao(@Named("DynamoDBMapper") DynamoDBMapper mapper) {
        return new NonCachingIntoleranceDao(mapper);
    }

    @Singleton
    @Provides
    @Named("RecipeDao")
    @Inject
    public RecipeDao provideRecipeDao() {
        return new RecipeDao();
    }

}
