package com.eldritch.quiz;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Lobby {
    private final String id;
    private final ConcurrentHashMap<String, QuizPlayer> players = new ConcurrentHashMap<>();
    private final QuizService gameInstance;
    private QuizPlayer lastJoinedPlayer;

    @Autowired
    public Lobby(SimpMessagingTemplate messagingTemplate,
                 ObjectProvider<QuizService> quizServiceProvider) {
        this.id = UUID.randomUUID().toString();  // Generate ID in constructor
        this.gameInstance = quizServiceProvider.getObject(messagingTemplate, this.id);
        // Remove setMessagingTemplate() call since it's now constructor-injected
    }

    // Remove setId() since ID is now final and set in constructor
    public String getId() { return id; }

    public QuizService getGameInstance() { return gameInstance; }

    public Collection<QuizPlayer> getPlayers() { return players.values(); }

    public QuizPlayer addPlayer(String nickname) {
        lastJoinedPlayer = gameInstance.addPlayer(nickname);
        players.put(lastJoinedPlayer.getId(), lastJoinedPlayer);
        return lastJoinedPlayer;
    }

    public QuizPlayer getLastJoinedPlayer() {
        return lastJoinedPlayer;
    }

    public void removePlayer(String playerId) { players.remove(playerId); }

    public boolean isFull() {
        return gameInstance.isQuizRunning();
    }
}