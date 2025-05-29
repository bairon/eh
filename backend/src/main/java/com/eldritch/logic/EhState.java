package com.eldritch.logic;

import java.util.List;

public class EhState {
    private EhStatus status;
    private List<Investigator> investigators;


    public EhStatus getStatus() {
        return status;
    }

    public void setStatus(EhStatus status) {
        this.status = status;
    }

    public List<Investigator> getInvestigators() {
        return investigators;
    }

    public void setInvestigators(List<Investigator> investigators) {
        this.investigators = investigators;
    }
}
