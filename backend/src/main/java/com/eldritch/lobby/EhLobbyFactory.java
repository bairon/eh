package com.eldritch.lobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EhLobbyFactory {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final EhServerFactory ehServerFactory;

    public EhLobbyFactory(SimpMessagingTemplate messagingTemplate, EhServerFactory ehServerFactory) {
        this.messagingTemplate = messagingTemplate;
        this.ehServerFactory = ehServerFactory;
    }

    public EhLobby createLobby(String gameName) {
        return new EhLobby(this.messagingTemplate, ehServerFactory, gameName);
    }

    public SimpMessagingTemplate getMessagingTemplate() {
        return messagingTemplate;
    }
}
