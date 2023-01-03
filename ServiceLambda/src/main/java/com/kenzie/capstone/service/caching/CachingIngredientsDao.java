package com.kenzie.capstone.service.caching;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kenzie.capstone.service.dao.IngredientsDao;
import com.kenzie.capstone.service.dao.NonCachingIngredientsDao;
import com.kenzie.capstone.service.model.IngredientsData;
import com.kenzie.capstone.service.model.IngredientsRecord;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CachingIngredientsDao implements IngredientsDao {

    private static final int INGREDIENTS_READ_TTL = 60 * 60;
    private static final String INGREDIENTS_KEY = "IngredientsKey::";

    private final CacheClient cacheClient;
    private final NonCachingIngredientsDao ingredientsDao;

    @Inject
    public CachingIngredientsDao(CacheClient cacheClient, NonCachingIngredientsDao ingredientsDao) {
        this.cacheClient = cacheClient;
        this.ingredientsDao = ingredientsDao;
    }

    public IngredientsData storeIngredientsData(IngredientsData data) {
        List<IngredientsRecord> records = getIngredientsData(data.getId());
        cacheClient.invalidate(data.getId());
        records.add(dataToRecord(data));
        addToCache(records);

        return data;
    }

    public List<IngredientsRecord> getIngredientsData(String id) {
        List<IngredientsRecord> records;
        if (cacheClient.getValue(id).isEmpty()) {
            records = ingredientsDao.getIngredientsData(id);
            addToCache(records);
        } else {
            records = fromJson(cacheClient.getValue(id).get());
        }

        return records;
    }

    public IngredientsRecord setIngredientsData(String id, String data) {
        List<IngredientsRecord> records = getIngredientsData(id);
        cacheClient.invalidate(id);
        records.add(createRecord(id, data));
        addToCache(records);

        return createRecord(id, data);
    }

    private List<IngredientsRecord> fromJson(String json) {
        return gson.fromJson(json, new TypeToken<ArrayList<IngredientsRecord>>() { }.getType());
    }

    private void addToCache(List<IngredientsRecord> records) {
        cacheClient.setValue(INGREDIENTS_KEY + records.get(0).getId(),
                INGREDIENTS_READ_TTL,
                gson.toJson(records));
    }

    private IngredientsRecord dataToRecord(IngredientsData data) {
        IngredientsRecord record = new IngredientsRecord();
        record.setData(data.getData());
        record.setId(data.getId());
        return record;
    }

    private IngredientsRecord createRecord(String id, String data) {
        IngredientsRecord record = new IngredientsRecord();
        record.setData(data);
        record.setId(id);
        return record;
    }

    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(
            ZonedDateTime.class,
            new TypeAdapter<ZonedDateTime>() {
                @Override
                public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                    out.value(value.toString());
                }

                @Override
                public ZonedDateTime read(JsonReader in) throws IOException {
                    return ZonedDateTime.parse(in.nextString());
                }
            }
    ).enableComplexMapKeySerialization();

    Gson gson = builder.create();
}
