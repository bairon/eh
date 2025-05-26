package com.eldritch.lobby;

import com.eldritch.logic.Interraction;

import java.util.concurrent.CompletableFuture;

public abstract class EhAgent implements InterractionListener {
    protected final String id;
    protected String nickname;
    protected String investigatorId;
    protected String investigatorName;
    protected boolean master;

    public abstract CompletableFuture<String> interract(Interraction interraction);

    public EhAgent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(String investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getInvestigatorName() {
        return investigatorName;
    }

    public void setInvestigatorName(String investigatorName) {
        this.investigatorName = investigatorName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}
