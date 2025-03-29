package com.eldritch.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ClueService {
    public static final int CLUES_SIZE = 36; // Number of portals

    @Value("${clues.file}")
    private String cluesFile; // Base name of the properties file (e.g., "portals")

    private static final Logger logger = LoggerFactory.getLogger(ClueService.class);

    @Cacheable(value = "clues")
    public List<Clue> getClues() {
        String fileName = cluesFile + ".properties";
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }

        List<Clue> clues = new ArrayList<>();
        for (int i = 1; i <= CLUES_SIZE; i++) {
            String id = "clue" + i;
            String cityId = properties.getProperty(id + ".cityid");
            String image = properties.getProperty(id + ".image");
            String imageBack = properties.getProperty(id + ".imageback");
            clues.add(new Clue(cityId, image, imageBack));
        }

        return clues;
    }

    public void setCluesFile(String cluesFile) {
        this.cluesFile = cluesFile;
    }
}