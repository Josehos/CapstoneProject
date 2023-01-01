package com.kenzie.capstone.service.caching;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kenzie.capstone.service.dao.IntoleranceDao;
import com.kenzie.capstone.service.dao.NonCachingIntoleranceDao;
import com.kenzie.capstone.service.model.IntoleranceData;
import com.kenzie.capstone.service.model.IntoleranceRecord;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CachingIntoleranceDao implements IntoleranceDao {

    private static final int INTOLERANCE_READ_TTL = 60 * 60;
    private static final String INTOLERANCE_KEY = "IntoleranceKey::";

    private final CacheClient cacheClient;
    private final NonCachingIntoleranceDao intoleranceDao;

    @Inject
    public CachingIntoleranceDao(CacheClient cacheClient, NonCachingIntoleranceDao intoleranceDao) {
        this.cacheClient = cacheClient;
        this.intoleranceDao = intoleranceDao;
    }

    @Override
    public IntoleranceData storeIntoleranceData(IntoleranceData data) {
        List<IntoleranceRecord> records = getIntoleranceData(data.getId());
        cacheClient.invalidate(data.getId());
        records.add(dataToRecord(data));
        addToCache(records);

        return data;
    }

    public List<IntoleranceRecord> getIntoleranceData(String id) {
        List<IntoleranceRecord> records;
        if (cacheClient.getValue(id).isEmpty()) {
            records = intoleranceDao.getIntoleranceData(id);
            addToCache(records);
        } else {
            records = fromJson(cacheClient.getValue(id).get());
        }

        return records;
    }

    public IntoleranceRecord setIntoleranceData(String id, String data) {
        List<IntoleranceRecord> records = getIntoleranceData(id);
        cacheClient.invalidate(id);
        records.add(createRecord(id, data));
        addToCache(records);

        return createRecord(id, data);
    }

    private List<IntoleranceRecord> fromJson(String json) {
        return gson.fromJson(json, new TypeToken<ArrayList<IntoleranceRecord>>() { }.getType());
    }

    private void addToCache(List<IntoleranceRecord> records) {
        cacheClient.setValue(INTOLERANCE_KEY + records.get(0).getId(),
                INTOLERANCE_READ_TTL,
                gson.toJson(records));
    }

    private IntoleranceRecord dataToRecord(IntoleranceData data) {
        IntoleranceRecord record = new IntoleranceRecord();
        record.setData(data.getData());
        record.setId(data.getId());
        return record;
    }

    private IntoleranceRecord createRecord(String id, String data) {
        IntoleranceRecord record = new IntoleranceRecord();
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
