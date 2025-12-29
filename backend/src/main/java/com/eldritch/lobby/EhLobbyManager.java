package com.eldritch.lobby;

import com.eldritch.user.UserData;
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
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EhLobbyManager implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LogManager.getLogger(EhLobbyManager.class);

    private final ConcurrentHashMap<String, EhLobby> activeLobbies = new ConcurrentHashMap<>();

    @Autowired
    private final EhLobbyFactory ehLobbyFactory;


    public EhLobbyManager(EhLobbyFactory ehLobbyFactory) {
        this.ehLobbyFactory = ehLobbyFactory;
    }

    public EhLobby getLobby(String lobbyId) {
        return activeLobbies.get(lobbyId);
    }
    public List<LobbyInfo> list() {
        return activeLobbies.values().stream().filter(lobby -> !lobby.getServer().isStarted()).map(EhLobby::info).toList();
    }

    public EhLobby create(String gameName, UserData userData) {
        EhLobby existingLobby = findByUserId(userData.getId());
        if (existingLobby != null) {
            return existingLobby;
        } else {
            EhLobby lobby = ehLobbyFactory.createLobby(gameName);
            activeLobbies.put(lobby.getId(), lobby);
            HumanAgent agent = new HumanAgent(userData);
            agent.setMaster(true);
            lobby.addAgent(agent);
            return lobby;
        }

    }

    public EhLobby join(String lobbyId, UserData userData) {
        EhLobby lobby = activeLobbies.get(lobbyId);

        if (lobby != null) {
            lobby.addAgent(new HumanAgent(userData));
        }
        return lobby;
    }

    public EhLobby leave(String userId) {
        EhLobby lobby = findByUserId(userId);
        if (lobby == null) return null;
        lobby.removeAgent(userId);

        if (lobby.isEmpty()) {
            lobby.terminate();
            activeLobbies.remove(lobby.getId());
            return null;
        }
        return lobby;
    }

    public EhLobby kick(String kickUserId, String byUserId) {
        if (Objects.equals(kickUserId, byUserId)) return null;
        EhLobby byLobby = findByUserId(byUserId);
        if (byLobby == null) return null;
        EhLobby kickLobby = findByUserId(kickUserId);
        if (kickLobby == null) return null;
        if (byLobby.getId().equals(kickLobby.getId())) {
            kickLobby.removeAgent(kickUserId);
            return kickLobby;
        }
        return null;
    }

    public EhLobby findByUserId(String id) {

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