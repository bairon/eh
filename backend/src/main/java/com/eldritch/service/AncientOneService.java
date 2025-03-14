package com.eldritch.service;

import com.eldritch.model.AncientOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Service
public class AncientOneService {
    public static final int ANCIENT_ONE_SIZE = 2;

    @Value("${ancientones.file}")
    private String ancientOnesFile;

    private static final Logger logger = LoggerFactory.getLogger(AncientOneService.class);

    public List<AncientOne> getAncientOnes(Locale locale) {
        // Construct the full file name with locale suffix
        String fileName = ancientOnesFile + "_" + locale.getLanguage() + ".properties";
        logger.info("Loading ancient ones from: {}", fileName);

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }

        List<AncientOne> ancientOnes = new ArrayList<>();
        for (int i = 1; i <= ANCIENT_ONE_SIZE; i++) {
            String name = properties.getProperty("ancientone" + i + ".name");
            String image1 = properties.getProperty("ancientone" + i + ".image1");
            String image2 = properties.getProperty("ancientone" + i + ".image2");
            String description = properties.getProperty("ancientone" + i + ".description");
            ancientOnes.add(new AncientOne(name, image1, image2, description));
        }

        return ancientOnes;
    }
}