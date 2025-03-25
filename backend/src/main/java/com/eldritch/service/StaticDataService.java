package com.eldritch.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaticDataService {

    @Cacheable(value = "staticData", key = "#locale.toString()")
    public Map<String, String> getAllStaticData(Locale locale) {

        ResourceBundle bundle = ResourceBundle.getBundle("static_data", locale);
        Enumeration<String> keys = bundle.getKeys();

        Map<String, String> result = new HashMap<>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String message = bundle.getString(key);
            result.put(key, message);
        }
        return result;

    }
}
