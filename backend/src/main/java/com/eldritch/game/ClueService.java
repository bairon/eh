package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ClueService {
    public static final int CLUES_SIZE = 36;

    public List<Clue> getClues() {
        Properties properties = ConfigService.getProperties("clues.properties");
        List<Clue> clues = new ArrayList<>();

        for (int i = 1; i <= CLUES_SIZE; i++) {
            String prefix = "clue" + i + ".";
            String id = properties.getProperty(prefix + "id");
            String cityId = properties.getProperty(prefix + "cityid");
            String image = properties.getProperty(prefix + "image");
            String imageBack = properties.getProperty(prefix + "imageback");

            clues.add(new Clue(id, cityId, image, imageBack));
        }

        return clues;
    }
}