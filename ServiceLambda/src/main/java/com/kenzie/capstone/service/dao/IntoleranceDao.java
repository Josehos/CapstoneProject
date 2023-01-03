package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.IntoleranceData;
import com.kenzie.capstone.service.model.IntoleranceRecord;

import java.util.List;

public interface IntoleranceDao {

    IntoleranceData storeIntoleranceData(IntoleranceData data);
    List<IntoleranceRecord> getIntoleranceData(String id);
    IntoleranceRecord setIntoleranceData(String id, String data);
}
