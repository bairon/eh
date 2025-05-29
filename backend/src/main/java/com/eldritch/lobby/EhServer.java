package com.eldritch.lobby;

import com.eldritch.logic.EhLogic;
import com.eldritch.logic.EhState;
import com.eldritch.logic.EhStatus;
import com.eldritch.quiz.QuizLobbyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class EhServer implements InterractionListener {

    // Infrastructure
    private static final Logger logger = LogManager.getLogger(QuizLobbyManager.class);
    private final SimpMessagingTemplate messagingTemplate;
    private Thread serverThread;


    //State
    private final String lobbyId;
    private EhLogic ehLogic;
    private final List<EhAgent> agents = new ArrayList<>();
    private EhAgent currentAgent;
    private int waitTimeout;

    public EhServer(SimpMessagingTemplate messagingTemplate, String lobbyId) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyId = lobbyId;
        this.ehLogic = new EhLogic(this, new EhState(), agents);
    }

    public synchronized EhAgent addAgent(EhAgent agent) {
        agents.add(agent);
        return agent;
    }

    public synchronized Optional<EhAgent> getAgent(String id) {
        return agents.stream().filter(agent -> agent.getId().equals(id)).findFirst();
    }
    public synchronized void removeAgent(String userId) {
        agents.removeIf(agent -> agent.getId().equals(userId));
        agents.getFirst().setMaster(true);

    }

    public void startServer() {
        logger.info("Starting EH Server...");
        ehLogic.init();
        this.serverThread = new Thread(() -> {
            while (ehLogic.inProgress()) {
                try {
                    waitTimeout = ehLogic.runCycle(ehLogic.getState());
                    if (waitTimeout > 0) {
                        synchronized (this) {
                            wait(waitTimeout);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            messagingTemplate.convertAndSend("/topic/ehgame/" + lobbyId, ehLogic.getState());
        });
        serverThread.start();
    }

    @Override
    public void onAnswerReceived(String playerId, String answer) {
        currentAgent.onAnswerReceived(playerId, answer);
    }

    public Map<String, Object> getCurrentState() {
        Map<String, Object> state = new HashMap<>();
        return state;
    }

    public void restoreState(Map<String, Object> state) {

    }

    public void stopServer() {
        this.serverThread.interrupt();
    }

    public boolean isFinished() {
        return ehLogic != null && ehLogic.isFinished();
    }

    public boolean isRunning() {
        return ehLogic != null && ehLogic.inProgress();
    }

    public boolean isStarted() {
        return ehLogic != null;
    }

    public EhStatus getStatus() {
        return ehLogic.getState().getStatus();
    }
}
