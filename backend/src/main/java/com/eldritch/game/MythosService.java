package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MythosService {
    private static final int BLUE_MYTHOS_COUNT = 12;
    private static final int GREEN_MYTHOS_COUNT = 18;
    private static final int YELLOW_MYTHOS_COUNT = 21;

    @Cacheable(value = "blue_mythos")
    public List<Mythos> getBlueMythos() {
        return loadMythos("mythos_blue.properties", "blue", BLUE_MYTHOS_COUNT);
    }

    @Cacheable(value = "green_mythos")
    public List<Mythos> getGreenMythos() {
        return loadMythos("mythos_green.properties", "green", GREEN_MYTHOS_COUNT);
    }

    @Cacheable(value = "yellow_mythos")
    public List<Mythos> getYellowMythos() {
        return loadMythos("mythos_yellow.properties", "yellow", YELLOW_MYTHOS_COUNT);
    }

    private List<Mythos> loadMythos(String filename, String color, int count) {
        Properties properties = ConfigService.getProperties(filename);
        List<Mythos> mythosList = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String prefix = "myth" + (i < 10 ? "0" + i : i) + ".";
            if (properties.containsKey(prefix + "id")) {
                mythosList.add(createMythos(properties, prefix, color));
            }
        }

        return mythosList;
    }

    private Mythos createMythos(Properties properties, String prefix, String color) {
        List<String> actions = new ArrayList<>();
        for (int i = 1; properties.containsKey(prefix + "action" + (i < 10 ? "0" + i : i)); i++) {
            actions.add(properties.getProperty(prefix + "action" + (i < 10 ? "0" + i : i)));
        }

        return new Mythos(
                properties.getProperty(prefix + "id"),
                color,
                actions,
                properties.getProperty(prefix + "onplay"),
                properties.getProperty(prefix + "reckoning"),
                properties.getProperty(prefix + "test"),
                properties.getProperty(prefix + "onsuccess"),
                properties.getProperty(prefix + "ontokenends"),
                properties.getProperty(prefix + "rumor"),
                properties.getProperty(prefix + "contact"),
                properties.getProperty(prefix + "discard")
        );
    }
}