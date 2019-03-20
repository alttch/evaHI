package com.altertech.evahi.core.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Created by oshevchuk on 07.05.2018
 */
public class SingletonMapper {

    private static volatile ObjectMapper instance1, instance2;

    private SingletonMapper() {
    }

    public static ObjectMapper getInstanceJson() {
        if (instance1 == null)
            synchronized (ObjectMapper.class) {
                if (instance1 == null) {
                    instance1 = new ObjectMapper();
                    instance1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        return instance1;
    }

    public static ObjectMapper getInstanceYaml() {
        if (instance2 == null)
            synchronized (ObjectMapper.class) {
                if (instance2 == null) {
                    instance2 = new ObjectMapper(new YAMLFactory());
                    instance2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        return instance2;
    }
}