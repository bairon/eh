package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class AssetService {
    public static final int ASSETS_SIZE = 40;

    public List<Asset> getAssets() {
        Properties properties = ConfigService.getProperties("assets.properties");
        List<Asset> assets = new ArrayList<>();

        for (int i = 1; i <= ASSETS_SIZE; i++) {
            String prefix = "asset" + String.format("%02d", i) + ".";
            String id = properties.getProperty(prefix + "id");
            String name = properties.getProperty(prefix + "name");
            String classification = properties.getProperty(prefix + "classification");
            String bonus = properties.getProperty(prefix + "bonus");
            String action = properties.getProperty(prefix + "action");
            String test = properties.getProperty(prefix + "test");
            String immediate = properties.getProperty(prefix + "immediate");
            int cost = Integer.parseInt(properties.getProperty(prefix + "cost", "0"));

            assets.add(new Asset(id, name, classification, bonus, action, test, immediate, cost));
        }

        return assets;
    }
}