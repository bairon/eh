package com.eldritch.investigator;

import com.eldritch.staticdata.ConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class InvestigatorService {
    public static final int INVESTIGATOR_SIZE = 12;

    //private static final Logger logger = LoggerFactory.getLogger(InvestigatorService.class);

    public List<InvestigatorTemplate> getInvestigators() {
        Properties properties = ConfigService.getProperties("investigators.properties");
        List<InvestigatorTemplate> investigators = new ArrayList<>();
        for (int i = 1; i <= INVESTIGATOR_SIZE; i++) {
            String id = properties.getProperty("investigator" + i + ".id");
            String image = properties.getProperty("investigator" + i + ".image");
            String imageback = properties.getProperty("investigator" + i + ".imageback");
            String description = properties.getProperty("investigator" + i + ".description");
            String location = properties.getProperty("investigator" + i + ".location");
            String property = properties.getProperty("investigator" + i + ".property");
            String bonus = properties.getProperty("investigator" + i + ".bonus");
            investigators.add(new InvestigatorTemplate(id, image, imageback, description, location, property, bonus));
        }

        return investigators;
    }
}