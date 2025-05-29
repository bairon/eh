package com.eldritch.investigator;

import com.eldritch.logic.Investigator;
import com.eldritch.staticdata.ConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class InvestigatorService {
    public static final int INVESTIGATOR_SIZE = 12;
    public static List<InvestigatorTemplate> investigatorsCache;
    public List<InvestigatorTemplate> getInvestigators() {
        if (investigatorsCache != null) return investigatorsCache;
        Properties properties = ConfigService.getProperties("investigators.properties");
        List<InvestigatorTemplate> investigators = new ArrayList<>();

        for (int i = 1; i <= INVESTIGATOR_SIZE; i++) {
            String prefix = "investigator" + i + ".";
            String id = properties.getProperty(prefix + "id");
            String image = properties.getProperty(prefix + "image");
            String imageback = properties.getProperty(prefix + "imageback");
            String description = properties.getProperty(prefix + "description");
            String location = properties.getProperty(prefix + "location");
            String bonus = properties.getProperty(prefix + "bonus", "");

            int lore = Integer.parseInt(properties.getProperty(prefix + "lore", "0"));
            int influence = Integer.parseInt(properties.getProperty(prefix + "influence", "0"));
            int observation = Integer.parseInt(properties.getProperty(prefix + "observation", "0"));
            int strength = Integer.parseInt(properties.getProperty(prefix + "strength", "0"));
            int will = Integer.parseInt(properties.getProperty(prefix + "will", "0"));

            String clues = properties.getProperty(prefix + "clues", "");
            String assets = properties.getProperty(prefix + "assets", "");
            String spells = properties.getProperty(prefix + "spells", "");

            investigators.add(new InvestigatorTemplate(
                    id, image, imageback, description,
                    location, bonus,
                    lore, influence, observation, strength, will,
                    clues, assets, spells
            ));
        }
        investigatorsCache = investigators;
        return investigatorsCache;
    }

    private Optional<InvestigatorTemplate> getInvestigatorTemplate(String investigatorId) {
        return getInvestigators().stream().filter(invTemplate -> investigatorId.equals(invTemplate.getId())).findFirst();
    }

    public Investigator create(String investigatorId) {
        Optional<InvestigatorTemplate> investigatorTemplate = getInvestigatorTemplate(investigatorId);
        if (investigatorTemplate.isPresent()) {
            return new Investigator(investigatorTemplate.get());
        } else {
            throw new RuntimeException("Wrong configuration in investigator id " + investigatorId);
        }
    }
}