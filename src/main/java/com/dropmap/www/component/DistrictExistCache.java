package com.dropmap.www.component;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DistrictExistCache {
    private final Map<String, Boolean> existCache = new ConcurrentHashMap<>();

    public boolean isExist(String key) {
        return existCache.getOrDefault(key, false);
    }

    public void put(String key, boolean value) {
        existCache.put(key, value);
    }

    public boolean contains(String key) {
        return existCache.containsKey(key);
    }

    public void clear() {
        existCache.clear();
    }
}
