package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.IngredientsDao;
import com.kenzie.capstone.service.dao.IntoleranceDao;
import com.kenzie.capstone.service.dao.RecipeDao;
import com.kenzie.capstone.service.model.IngredientsData;
import com.kenzie.capstone.service.model.IngredientsRecord;
import com.kenzie.capstone.service.model.IntoleranceData;
import com.kenzie.capstone.service.model.IntoleranceRecord;


import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class LambdaService {


    private IngredientsDao ingredientsDao;
    private IntoleranceDao intoleranceDao;
    private RecipeDao recipeDao;


    @Inject
    public LambdaService(RecipeDao recipeDao) {
        this.ingredientsDao = ingredientsDao;
        this.intoleranceDao = intoleranceDao;
        this.recipeDao = recipeDao;
    }

//    public IngredientsData getIngredientsData(String id) {
//        List<IngredientsRecord> records = ingredientsDao.getIngredientsData(id);
//        if (records.size() > 0) {
//            return new IngredientsData(records.get(0).getId(), records.get(0).getData());
//        }
//        return null;
//    }
//
//    public IngredientsData setIngredientsData(String data) {
//        String id = UUID.randomUUID().toString();
//        IngredientsRecord record = ingredientsDao.setIngredientsData(id, data);
//        return new IngredientsData(id, data);
//    }
//
//    public IntoleranceData getIntoleranceData(String id) {
//        List<IntoleranceRecord> records = intoleranceDao.getIntoleranceData(id);
//        if (records.size() > 0) {
//            return new IntoleranceData(records.get(0).getId(), records.get(0).getData());
//        }
//        return null;
//    }

    public IntoleranceData setIntoleranceData(String data) {
        String id = UUID.randomUUID().toString();
        IntoleranceRecord record = intoleranceDao.setIntoleranceData(id, data);
        return new IntoleranceData(id, data);
    }

    public String getRecipesByRestrictions(String dietaryRestriction) {

        return recipeDao.getRecipesByRestrictions(dietaryRestriction);
    }

    public String getRecipesByIngredients(String ingredients) {

        return recipeDao.getRecipesByIngredients(ingredients);
    }
}
