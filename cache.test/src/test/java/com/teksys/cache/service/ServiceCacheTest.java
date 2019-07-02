package com.teksys.cache.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceCacheTest {

    private Service service;

    @Before
    public void setUp() {
        this.service = new ServiceCacheImpl();
    }

    @Test
    public void testPut() {
        this.service.put("test1", "test1");
        this.service.put("test2", "test2");
        //this test fails if the cache time is configured to 3 sec. where we have thread clean 
        // the data in the background on a timely manner
        Assert.assertEquals(2, this.service.size());
    }
}
