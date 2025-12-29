package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MysteryService {
    private static final int MYSTERIES_COUNT = 16;

    @Cacheable(value = "mysteries")
    public List<Mystery> getMysteries() {
        Properties properties = ConfigService.getProperties("mysteries.properties");
        List<Mystery> mysteries = new ArrayList<>();

        for (int i = 1; i <= MYSTERIES_COUNT; i++) {
            String prefix = "myst" + (i < 10 ? "0" + i : i) + ".";
            if (properties.containsKey(prefix + "id")) {
                mysteries.add(createMystery(properties, prefix));
            }
        }

        return mysteries;
    }

    private Mystery createMystery(Properties properties, String prefix) {
        return new Mystery(
                properties.getProperty(prefix + "id"),
                properties.getProperty(prefix + "onplay"),
                properties.getProperty(prefix + "when"),
                properties.getProperty(prefix + "contact"),
                properties.getProperty(prefix + "test"),
                properties.getProperty(prefix + "success"),
                properties.getProperty(prefix + "ondefeat"),
                properties.getProperty(prefix + "solved"),
                properties.getProperty(prefix + "onfinal")
        );
    }

    public List<Mystery> getMysteriesForAncientOne(String ancientOne) {
        return getMysteries().stream()
                .filter(m -> m.getAncientOne().equalsIgnoreCase(ancientOne))
                .toList();
    }
}