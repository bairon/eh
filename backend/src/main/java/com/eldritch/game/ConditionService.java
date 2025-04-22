package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ConditionService {
    public static final int CONDITIONS_SIZE = 36;

    public List<Condition> getConditions() {
        Properties properties = ConfigService.getProperties("conditions.properties");
        List<Condition> conditions = new ArrayList<>();

        for (int i = 1; i <= CONDITIONS_SIZE; i++) {
            String prefix = "condition" + (i < 10 ? "0" + i : i) + ".";
            conditions.add(createCondition(properties, prefix));
        }

        return conditions;
    }

    private Condition createCondition(Properties properties, String prefix) {
        String id = properties.getProperty(prefix + "id");
        String name = properties.getProperty(prefix + "name");
        String action = properties.getProperty(prefix + "action");
        String basetest = properties.getProperty(prefix + "basetest");
        String basesuccess = properties.getProperty(prefix + "basesuccess");
        String basefailure = properties.getProperty(prefix + "basefailure");
        String reckoningtest = properties.getProperty(prefix + "reckoningtest");
        String reckoningsuccess = properties.getProperty(prefix + "reckoningsuccess");
        String reckoningfailure = properties.getProperty(prefix + "reckoningfailure");
        String reckoningfinal = properties.getProperty(prefix + "reckoningfinal");
        String debuff = properties.getProperty(prefix + "debuff");
        String buff = properties.getProperty(prefix + "buff");
        String contact = properties.getProperty(prefix + "contact");
        String reckoningchoice = properties.getProperty(prefix + "reckoningchoice");

        return new Condition(
                id, name, action, basetest, basesuccess, basefailure,
                reckoningtest, reckoningsuccess, reckoningfailure, reckoningfinal,
                debuff, buff, contact, reckoningchoice
        );
    }
}