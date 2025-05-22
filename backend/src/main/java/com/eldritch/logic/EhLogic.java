package com.eldritch.logic;

import com.eldritch.lobby.EhAgent;
import com.eldritch.lobby.EhServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EhLogic {

    private static final Logger logger = LogManager.getLogger(EhLogic.class);
    private final EhServer server;
    private final EhState state;
    private final List<EhAgent> agents;

    public EhLogic(EhServer server, EhState state, List<EhAgent> agents) {
        this.server = server;
        this.state = state;
        this.agents = agents;
    }

    public void init() {

    }

    public int runCycle(EhState state) throws ExecutionException {
        logger.debug("Running EH sycle");

        return 0;
    }

    public boolean inProgress() {
        return EhStatus.IN_PROGRESS == state.getStatus();
    }

    public boolean isFinished() {
        return EhStatus.FINISHED == state.getStatus();
    }
}
