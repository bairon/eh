package com.eldritch.investigator;

public class InvestigatorFactory {
    public InvBase create(String id) {
        switch (id) {
            case "akachi": return new Akachi();
            case "charlie": return new Charlie();
            case "diana": return new Diana();
            case "jaklin": return new Jaklin();
            case "jim": return new Jim();
            case "leo": return new Leo();
            case "lili": return new Lili();
            case "lola": return new Lola();
            case "mark": return new Mark();
            case "normann": return new Normann();
            case "sailas": return new Sailas();
            case "trish": return new Trish();
        }
        return null;
    }
}
