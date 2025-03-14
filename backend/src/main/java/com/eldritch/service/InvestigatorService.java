package com.eldritch.service;

import com.eldritch.model.Investigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Service
public class InvestigatorService {
    public static final int INVESTIGATOR_SIZE = 2;

    @Value("${investigators.file}")
    private String investigatorsFile;

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(InvestigatorService.class);

    public List<Investigator> getInvestigators(Locale locale) {
        // Construct the full file name with locale suffix
        String fileName = investigatorsFile + "_" + locale.getLanguage() + ".properties";
        logger.info("Loading investigators from: {}", fileName);

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }

        List<Investigator> investigators = new ArrayList<>();
        for (int i = 1; i <= INVESTIGATOR_SIZE; i++) {
            String name = properties.getProperty("investigator" + i + ".name");
            String image1 = properties.getProperty("investigator" + i + ".image1");
            String image2 = properties.getProperty("investigator" + i + ".image2");
            String description = properties.getProperty("investigator" + i + ".description");
            investigators.add(new Investigator(name, image1, image2, description));
        }

        return investigators;
    }
}