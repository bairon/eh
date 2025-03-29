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
public class PortalService {
    public static final int PORTAL_SIZE = 9; // Number of portals

    @Value("${portals.file}")
    private String portalsFile; // Base name of the properties file (e.g., "portals")

    private static final Logger logger = LoggerFactory.getLogger(PortalService.class);

    @Cacheable(value = "portals")
    public List<Portal> getPortals() {
        String fileName = portalsFile + ".properties";
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }

        List<Portal> portals = new ArrayList<>();
        for (int i = 1; i <= PORTAL_SIZE; i++) {
            String id = "portal" + i;
            String cityId = properties.getProperty(id + ".cityid");
            String image = properties.getProperty(id + ".image");
            String imageBack = properties.getProperty(id + ".imageback");
            portals.add(new Portal(cityId, image, imageBack));
        }

        return portals;
    }

    public void setPortalsFile(String portalsFile) {
        this.portalsFile = portalsFile;
    }
}