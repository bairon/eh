package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MonsterService {
    private static final int MONSTERS_COUNT = 43;

    @Cacheable(value = "normal_monsters")
    public List<Monster> getNormalMonsters() {
        return loadMonsters("monsters.properties", MONSTERS_COUNT);
    }

    @Cacheable(value = "epic_monsters")
    public List<Monster> getEpicMonsters() {
        return loadMonsters("monsters_epic.properties", MONSTERS_COUNT);
    }

    private List<Monster> loadMonsters(String filename, int count) {
        Properties properties = ConfigService.getProperties(filename);
        List<Monster> monsters = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String prefix = "monster" + (i < 10 ? "0" + i : i) + ".";
            if (properties.containsKey(prefix + "id")) {
                monsters.add(createMonster(properties, prefix));
            }
        }

        return monsters;
    }

    private Monster createMonster(Properties properties, String prefix) {
        return new Monster(
                properties.getProperty(prefix + "id"),
                properties.getProperty(prefix + "name"),
                getIntegerProperty(properties, prefix + "testwillmodifier"),
                getIntegerProperty(properties, prefix + "teststrengthmodifier"),
                getIntegerProperty(properties, prefix + "horror"),
                getIntegerProperty(properties, prefix + "damage"),
                parseToughnessBase(properties.getProperty(prefix + "toughnessbase")),
                getIntegerProperty(properties, prefix + "toughnessmodifier"),
                properties.getProperty(prefix + "special"),
                properties.getProperty(prefix + "reckoning"),
                properties.getProperty(prefix + "onspawn"),
                properties.getProperty(prefix + "ondefeat"),
                properties.getProperty(prefix + "movement")
        );
    }

    private Integer getIntegerProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        return value != null && !value.isEmpty() ? Integer.parseInt(value) : null;
    }

    private Integer parseToughnessBase(String value) {
        if (value == null || value.isEmpty()) return null;
        if (value.equals("investigators_count")) return 0; // Will be calculated dynamically
        return Integer.parseInt(value);
    }
}