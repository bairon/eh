package com.eldritch.ancientone;

import com.eldritch.staticdata.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Service
public class AncientOneService {
    public static final int ANCIENT_ONE_SIZE = 4;

    private static final Logger logger = LoggerFactory.getLogger(AncientOneService.class);

    public List<AncientOne> getAncientOnes() {

        Properties properties = ConfigService.getProperties("ancient_ones.properties");
        List<AncientOne> ancientOnes = new ArrayList<>();
        for (int i = 1; i <= ANCIENT_ONE_SIZE; i++) {
            String id = properties.getProperty("ancientone" + i + ".id");
            String image = properties.getProperty("ancientone" + i + ".image");
            String imageback = properties.getProperty("ancientone" + i + ".imageback");
            String description = properties.getProperty("ancientone" + i + ".description");
            String doom = properties.getProperty("ancientone" + i + ".doom");
            ancientOnes.add(new AncientOne(id, image, imageback, description, Integer.parseInt(doom)));
        }

        return ancientOnes;
    }
}