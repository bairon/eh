package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class LocationService {
    public static final int LOCATIONS_SIZE = 36;

    @Cacheable(value = "locations")
    public List<Location> getLocations() {
        Properties properties = ConfigService.getProperties("locations.properties");
        List<Location> locations = new ArrayList<>();

        // Process numbered locations (location01-location21)
        for (int i = 1; i <= 21; i++) {
            String prefix = "location" + (i < 10 ? "0" + i : i) + ".";
            Location location = addLocation(properties, prefix);
            locations.add(location);
        }

        // Process named cities (location22-location36)
        for (int i = 22; i <= LOCATIONS_SIZE; i++) {
            String prefix = "location" + i + ".";
            Location location = addLocation(properties, prefix);
            locations.add(location);
        }

        return locations;
    }

    private Location addLocation(Properties properties, String prefix) {
        String id = properties.getProperty(prefix + "id");
        String train = properties.getProperty(prefix + "train");
        String ship = properties.getProperty(prefix + "ship");
        String uncharted = properties.getProperty(prefix + "uncharted");
        String space = properties.getProperty(prefix + "space");


        // Initialize connections list by combining train, ship, and uncharted
        List<String> connections = new ArrayList<>();

        if (train != null && !train.isEmpty()) {
            connections.addAll(Arrays.asList(train.split(",")));
        }

        if (ship != null && !ship.isEmpty()) {
            connections.addAll(Arrays.asList(ship.split(",")));
        }

        if (uncharted != null && !uncharted.isEmpty()) {
            connections.addAll(Arrays.asList(uncharted.split(",")));
        }

        return new Location(id, train, ship, uncharted, space, connections);
    }
}