package com.teksys.cache.service;

import com.teksys.cache.model.Cache;

import java.lang.ref.SoftReference;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCacheImpl implements Service {

    // we control the cache clean time based on the requirement
    private static final int CACHE_CLEAN_TIME = 30;
    private ConcurrentHashMap<String, SoftReference<Cache>> cache = new ConcurrentHashMap<>();

    public ServiceCacheImpl() {
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(CACHE_CLEAN_TIME * 1000);
                    cache.entrySet().removeIf(entry -> Optional.ofNullable(entry.getValue()).map(SoftReference::get)
                            .map(Cache::isExpired).orElse(false));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public Object get(String key) {
        return Optional.ofNullable(cache.get(key)).map(
                SoftReference::get).filter(c -> !c.isExpired()).map(Cache::getValue).orElse(null);
    }

    public void put(String key, Object value) {
        if (key == null) {
            return;
        }
        if (value == null) {
            cache.remove(key);
        } else {
            //we can configuration expiryTime based usecase 
            long expiryTime = System.currentTimeMillis() + CACHE_CLEAN_TIME; 
            cache.put(key, new SoftReference<>(new Cache(value, expiryTime)));
        }
    }

    @Override
    public long size() {
        return cache.entrySet().stream().filter(entry -> Optional.ofNullable(entry.getValue()).map(
                SoftReference::get).map(cacheObject -> !cacheObject.isExpired()).orElse(false)).count();
    }


}
