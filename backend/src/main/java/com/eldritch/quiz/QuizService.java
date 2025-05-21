package com.eldritch.quiz;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.concurrent.*;

public class QuizService implements QuizAnswerListener {

    // Configuration Constants
    public static final int QUIZ_WIN_SCORE = 2;
    private static final int QUIZ_PLAYERS_REQUIRED = 2;
    private static final int TEN_SECONDS = 10000;
    private static final int FIVE_SECONDS = 5000;

    // Infrastructure
    private final SimpMessagingTemplate messagingTemplate;
    private final String lobbyId;

    //State
    private final ConcurrentMap<String, QuizPlayer> players = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, QuizAgent> agents = new ConcurrentHashMap<>();
    private final Queue<QuizPlayer> playerQueue = new ConcurrentLinkedQueue<>();
    private QuizPlayer currentPlayer;
    private QuizAgent currentAgent;
    private boolean quizRunning = false;
    private boolean quizFinished;
    private QuizQuestion currentQuestion;
    private boolean answerGiven;
    private int answerIndex = 1;
    private boolean answerCorrect;
    private QuizMessage message;
    private QuizPlayer winner;
    private int waitTimeout;

    private final List<QuizQuestion> questions = List.of(
            new QuizQuestion("What is the capital of France?",
                    List.of("London", "Paris", "Berlin", "Madrid"), 1),
            new QuizQuestion("Which planet is known as the Red Planet?",
                    List.of("Venus", "Mars", "Jupiter", "Saturn"), 1),
            new QuizQuestion("What is 2+2?",
                    List.of("3", "4", "5", "6"), 1),
            new QuizQuestion("Who painted the Mona Lisa?",
                    List.of("Van Gogh", "Picasso", "Da Vinci", "Michelangelo"), 2),
            new QuizQuestion("What is the largest ocean on Earth?",
                    List.of("Atlantic", "Indian", "Arctic", "Pacific"), 3)
    );

    public QuizService(SimpMessagingTemplate messagingTemplate, String lobbyId) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyId = lobbyId;
    }

    public synchronized QuizPlayer addPlayer(String nickname) {
        String id = UUID.randomUUID().toString();
        QuizPlayer player = new QuizPlayer(id, nickname, 0, true); // active by default
        players.put(id, player);
        agents.put(id, new QuizHumanAgent(id));
        playerQueue.add(player);
        currentPlayer = player;
        prepareJoinMessage();

        // Start quiz if we have enough players
        if (players.size() >= QUIZ_PLAYERS_REQUIRED && !quizRunning) {
            startQuiz();
        }

        return player;
    }

    public QuizPlayer getPlayer(String playerId) {
        return players.get(playerId);
    }
    public ArrayList<QuizPlayer> getActivePlayers() {
        return new ArrayList<>(players.values());
    }

    public void startQuiz() {
        System.out.println("Starting Quiz");
        quizRunning = true;
        quizFinished = false;

        Thread quizThread = new Thread(() -> {
            while (quizRunning) {
                    try {
                        quizCycle();
                        if (waitTimeout > 0) {
                            synchronized (this) {
                                wait(waitTimeout);
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
            }
            prepareTerminateMessage();
            messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);

        });
        quizThread.start();
    }

    private void prepareTerminateMessage() {
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.TERMINATE);
    }

    public void quizCycle() throws ExecutionException, InterruptedException {
        if (quizRunning) {
            waitTimeout = TEN_SECONDS;
            if (currentPlayer != null && currentPlayer.getScore() >= QUIZ_WIN_SCORE) {
                quizRunning = false;
                quizFinished = true;
                winner = currentPlayer;
                currentPlayer = null;
                prepareWinMessage();
            } else if (currentQuestion != null && answerGiven) {
                answerCorrect = answerIndex == currentQuestion.getCorrectOption();
                if (answerCorrect) {
                    currentPlayer.incrementScore();
                }
                prepareAnswerMessage();
                currentQuestion = null;
                answerIndex = -1;
                answerGiven = false;
                waitTimeout = FIVE_SECONDS;
            } else if (currentQuestion == null) {
                // Get next player
                currentPlayer = playerQueue.poll();
                // Set all players inactive first
                players.values().forEach(p -> p.setActive(false));
                // Set new current player active
                currentPlayer.setActive(true);
                playerQueue.add(currentPlayer);
                currentAgent = agents.get(currentPlayer.getId());
                currentQuestion = getRandomQuestion();
                prepareQuestionMessage();
            }

            messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);

            if (currentQuestion != null && !answerGiven) {
                CompletableFuture<Integer> waitingAnswer = currentAgent.handleQuestion(currentQuestion);
                Integer answer = waitingAnswer.get();
                if (answer >= 0) {
                    answerGiven = true;
                    answerIndex = answer;
                    waitTimeout = 0;
                }
            }
        }
    }

    @Override
    public void onAnswerReceived(String playerId, int answerIndex) {
        currentAgent.onAnswerReceived(playerId, answerIndex);
    }
    private void prepareJoinMessage() {
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.JOIN);
        message.setPlayer(currentPlayer);
        message.setPlayers(new ArrayList<>(players.values()));
    }

    private void prepareQuestionMessage() {
        System.out.println("Preparing question " + currentQuestion + " to a player " + currentPlayer);
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.QUESTION);
        message.setPlayer(currentPlayer);
        message.setPlayers(new ArrayList<>(players.values()));
        message.setQuestion(currentQuestion);
    }

    private void prepareAnswerMessage() {
        System.out.println("Preparing answer message " + currentQuestion + " correct: " + answerCorrect);
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.ANSWER);
        message.setPlayer(currentPlayer);
        message.setSelectedAnswer(answerIndex);
        message.setCorrect(answerCorrect);
        message.setQuestion(currentQuestion);
        message.setPlayers(new ArrayList<>(players.values()));
    }

    private void prepareWinMessage() {
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.WIN);
        message.setWinner(winner);

    }

    private QuizQuestion getRandomQuestion() {
        Random random = new Random();
        return questions.get(random.nextInt(questions.size()));
    }

    public QuizQuestion getCurrentQuestion() {
        return currentQuestion; // Return the stored current question
    }

    public Collection<QuizPlayer> getPlayers() {
        return players.values();
    }
    public boolean isQuizRunning() {
        return quizRunning;
    }

    public boolean isQuizFinished() {
        return quizFinished;
    }

    public QuizPlayer getCurrentPlayer() {
        return currentPlayer;
    }
    public String getLobbyId() {
        return lobbyId;
    }

    public QuizMessage getMessage() {
        return message;
    }

    public Map<String, Object> getCurrentState() {
        Map<String, Object> state = new HashMap<>();
        state.put("quizRunning", quizRunning);
        state.put("quizFinished", quizFinished);
        state.put("currentPlayerId", currentPlayer != null ? currentPlayer.getId() : null);
        state.put("currentQuestion", currentQuestion);
        state.put("answerGiven", answerGiven);
        state.put("answerIndex", answerIndex);
        state.put("answerCorrect", answerCorrect);
        state.put("winnerId", winner != null ? winner.getId() : null);
        state.put("currentMessage", message != null ? message.toMap() : null);

        // Save players
        List<Map<String, Object>> playersState = new ArrayList<>();
        for (QuizPlayer player : players.values()) {
            Map<String, Object> playerState = new HashMap<>();
            playerState.put("id", player.getId());
            playerState.put("nickname", player.getNickname());
            playerState.put("score", player.getScore());
            playerState.put("active", player.isActive());
            playersState.add(playerState);
        }
        state.put("players", playersState);

        return state;
    }

    public void restoreState(Map<String, Object> state) {
        this.quizRunning = (boolean) state.get("quizRunning");

        // Restore players and agents
        List<Map<String, Object>> playersState = (List<Map<String, Object>>) state.get("players");
        players.clear();
        agents.clear();
        playerQueue.clear();

        for (Map<String, Object> playerState : playersState) {
            QuizPlayer player = new QuizPlayer(
                    (String) playerState.get("id"),
                    (String) playerState.get("nickname"),
                    ((Number) playerState.get("score")).intValue(),
                    (boolean) playerState.get("active")
            );
            players.put(player.getId(), player);

            // Restore agent - important fix!
            agents.put(player.getId(), new QuizHumanAgent(player.getId()));
            playerQueue.add(player);

            if (player.isActive()) {
                currentPlayer = player;
                currentAgent = agents.get(player.getId()); // Set currentAgent
            }
        }

        // Restore other state
        currentQuestion = QuizQuestion.fromMap((Map<String, Object>) state.get("currentQuestion"));
        answerGiven = (boolean) state.get("answerGiven");
        answerIndex = ((Long) state.get("answerIndex")).intValue();
        answerCorrect = (boolean) state.get("answerCorrect");

        String winnerId = (String) state.get("winnerId");
        if (winnerId != null) {
            winner = players.get(winnerId);
        }

        Map<String, Object> messageMap = (Map<String, Object>) state.get("currentMessage");
        this.message = QuizMessage.fromMap(messageMap);

        if (quizRunning) {
            if (message == null) {
                throw new RuntimeException("Quiz running without message");
            }
            // Restore message
            startQuiz();
        }
    }
}