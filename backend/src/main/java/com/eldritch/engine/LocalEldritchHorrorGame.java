package com.eldritch.engine;

import com.eldritch.client.LocalClientConnection;

import java.util.UUID;

public class LocalEldritchHorrorGame {
    private final GameServer server;
    private final LocalClientConnection clientConnection;

    public static void main(String[] args) {
        LocalEldritchHorrorGame game = new LocalEldritchHorrorGame();
        game.start();

    }
    public LocalEldritchHorrorGame() {
        this.server = new GameServer();
        this.clientConnection = new LocalClientConnection(UUID.randomUUID());

        server.registerClient(clientConnection);

    }

    public void start() {
        server.start();
    }

}