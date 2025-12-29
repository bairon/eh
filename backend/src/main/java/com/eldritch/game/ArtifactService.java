package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ArtifactService {
    public static final int ARTIFACTS_SIZE = 14;

    @Cacheable(value = "artifacts")
    public List<Artifact> getArtifacts() {
        Properties properties = ConfigService.getProperties("artifacts.properties");
        List<Artifact> artifacts = new ArrayList<>();

        for (int i = 1; i <= ARTIFACTS_SIZE; i++) {
            String prefix = "artifact" + (i < 10 ? "0" + i : i) + ".";
            artifacts.add(createArtifact(properties, prefix));
        }

        return artifacts;
    }

    private Artifact createArtifact(Properties properties, String prefix) {
        String id = properties.getProperty(prefix + "id");
        String name = properties.getProperty(prefix + "name");
        String classification = properties.getProperty(prefix + "classification");
        String action = properties.getProperty(prefix + "action");
        String bonus = properties.getProperty(prefix + "bonus");
        String immediate = properties.getProperty(prefix + "immediate");
        String test = properties.getProperty(prefix + "test");

        return new Artifact(id, name, classification, action, bonus, immediate, test);
    }
}