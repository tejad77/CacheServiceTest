package com.teksys.cache.service;

public interface Service {
    
    Object get(String key);
    
    void put(String key, Object value);
    
    long size();
}
