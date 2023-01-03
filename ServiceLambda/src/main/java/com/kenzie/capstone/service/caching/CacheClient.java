package com.kenzie.capstone.service.caching;

import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class CacheClient {

    public CacheClient() {}

    public void setValue(String key, int seconds, String value) {
        checkNonNullKey(key);
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        cache.setex(key, seconds, value);
        cache.close();
    }

    public Optional<String> getValue(String key) {
        checkNonNullKey(key);
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        Optional<String> value = Optional.ofNullable(cache.get(key));
        cache.close();
        return value;
    }

    public void invalidate(String key) {
        checkNonNullKey(key);
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        cache.del(key);
        cache.close();
    }

    private void checkNonNullKey(String key) {
        if (key == null) {
            throw new RuntimeException("Key must not be null!");
        }
    }
}
