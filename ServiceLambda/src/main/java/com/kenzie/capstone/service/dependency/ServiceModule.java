package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;

import com.kenzie.capstone.service.dao.IngredientsDao;
import com.kenzie.capstone.service.dao.IntoleranceDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public LambdaService provideLambdaService(@Named("IntoleranceDao") IntoleranceDao intoleranceDao,
                                              @Named("IngredientsDao") IngredientsDao ingredientsDao) {
        return new LambdaService(ingredientsDao, intoleranceDao);
    }
}

