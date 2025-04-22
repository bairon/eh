package com.eldritch.client;

import com.eldritch.common.PlayerChoiceRequest;
import com.eldritch.engine.GameAction;
import com.eldritch.engine.GameStateSnapshot;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class LocalClientConnection implements ClientConnection {
    private final UUID clientId;
    private final Scanner scanner = new Scanner(System.in);

    public LocalClientConnection(UUID clientId) {
        this.clientId = clientId;
    }

    @Override
    public UUID getClientId() {
        return clientId;
    }

    @Override
    public void gameStateUpdated(GameStateSnapshot state) {
        renderState(state);
    }

    private void renderState(GameStateSnapshot state) {
        System.out.println(state.getCurrentPhase());
        //ToDo Render state to a console
    }

    @Override
    public GameAction requestPlayerChoice(PlayerChoiceRequest request) {
        System.out.println("\n" + request.getPrompt());
        System.out.println("Available actions:");

        List<GameAction> actions = request.getAvailableActions();
        for (int i = 0; i < actions.size(); i++) {
            System.out.println((i + 1) + ". " + actions.get(i).getDescription());
        }

        System.out.print("Enter your choice (1-" + actions.size() + "): ");
        int choice = scanner.nextInt();

        // Validate input
        while (choice < 1 || choice > actions.size()) {
            System.out.print("Invalid choice. Please enter a number between 1 and " + actions.size() + ": ");
            choice = scanner.nextInt();
        }

        // Send the selected action back to the server
        GameAction selectedAction = actions.get(choice - 1);
        return selectedAction;
    }
}