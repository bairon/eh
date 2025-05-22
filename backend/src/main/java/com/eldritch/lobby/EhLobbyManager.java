package com.eldritch.lobby;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EhLobbyManager implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LogManager.getLogger(EhLobbyManager.class);

    private final ConcurrentHashMap<String, EhLobby> activeLobbies = new ConcurrentHashMap<>();
    private EhLobby availableLobby;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public EhLobbyManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        getAvailableLobby();
    }

    public synchronized EhLobby getAvailableLobby() {
        if (availableLobby == null) {
            availableLobby = new EhLobby(this.messagingTemplate);
        }
        return availableLobby;
    }

    public EhLobby getLobby(String lobbyId) {
        if (availableLobby != null && lobbyId.equals(availableLobby.getId())) {
            return availableLobby;
        }
        return activeLobbies.get(lobbyId);
    }

    public synchronized void lobbyStartedGame() {
        activeLobbies.put(availableLobby.getId(), availableLobby);
        availableLobby = null;
    }

    public List<LobbyInfo> list() {
        return activeLobbies.values().stream().filter(lobby -> !lobby.getServer().isStarted()).map(EhLobby::info).toList();
    }

    public EhLobby create(String gameName, String userId) {
        EhLobby existingLobby = findByUserId(userId);
        if (existingLobby != null) {
            return existingLobby;
        } else {
            return new EhLobby(this.messagingTemplate);
        }
    }

    public EhLobby join(String lobbyId, String nickname) {
        EhLobby ret = getAvailableLobby();
        ret.addAgent(nickname);

        if (ret.isFull()) {
            lobbyStartedGame();
        }
        return ret;
    }
    public EhLobby findByUserId(String id) {
        if (availableLobby != null && availableLobby.hasAgent(id)) {
            return availableLobby;
        }

        Iterator<Map.Entry<String, EhLobby>> iterator = activeLobbies.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, EhLobby> entry = iterator.next();
            EhLobby lobby = entry.getValue();

            if (lobby.getServer().isFinished()) {
                iterator.remove();
            } else if (lobby.hasAgent(id)) {
                return lobby;
            }
        }

        return null;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        saveAllLobbies();
    }

    public void saveAllLobbies() {
        logger.debug("Saving all lobbies");

    }
}