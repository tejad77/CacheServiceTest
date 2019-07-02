package com.teksys.cache.model;

public class Cache {
    
    public Object value;
    public long expiryTime;

    public Cache(Object value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }

    public Object getValue() {
        return value;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}
