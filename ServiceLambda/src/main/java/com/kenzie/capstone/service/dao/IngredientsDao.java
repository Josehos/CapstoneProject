package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.IngredientsData;
import com.kenzie.capstone.service.model.IngredientsRecord;

import java.util.List;

public interface IngredientsDao {
    IngredientsData storeIngredientsData(IngredientsData data);
    List<IngredientsRecord> getIngredientsData(String id);
    IngredientsRecord setIngredientsData(String id, String data);
}
