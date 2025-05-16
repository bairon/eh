package com.eldritch.quiz;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.concurrent.*;

public class QuizService implements QuizAnswerListener {

    // Configuration Constants
    public static final int QUIZ_WIN_SCORE = 5;
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
        });
        quizThread.start();
    }

    public void quizCycle() throws ExecutionException, InterruptedException {
        if (quizRunning) {
            sendPlayerUpdate();
            waitTimeout = TEN_SECONDS;
            if (currentPlayer != null && currentPlayer.getScore() >= QUIZ_WIN_SCORE) {
                quizRunning = false;
                winner = currentPlayer;
                currentPlayer = null;
                prepareWinMessage();
                messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
            } else if (currentQuestion != null && answerGiven) {
                answerCorrect = answerIndex == currentQuestion.getCorrectOption();
                if (answerCorrect) {
                    currentPlayer.incrementScore();
                }
                prepareAnswerMessage();
                messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
                currentQuestion = null;
                answerIndex = -1;
                answerGiven = false;
                waitTimeout = FIVE_SECONDS;
            } else if (currentQuestion == null) {
                if (currentPlayer != null) {
                    currentPlayer.setActive(false);
                }
                currentPlayer = playerQueue.poll();
                playerQueue.add(currentPlayer);
                currentAgent = agents.get(currentPlayer.getId());
                currentQuestion = getRandomQuestion();
                prepareQuestionMessage();
                messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
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

    private void prepareWinMessage() {
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.WIN);
        message.setWinner(winner);

    }

    private void sendPlayerUpdate() {
        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.UPDATE);
        message.setPlayers(new ArrayList<>(players.values()));
        messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
    }

    private void prepareQuestionMessage() {
        System.out.println("Preparing question " + currentQuestion + " to a player " + currentPlayer);
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.QUESTION);
        message.setPlayer(currentPlayer);
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
        messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
    }

    public void endQuiz(QuizPlayer winner) {
        quizRunning = false;
        message = new QuizMessage();
        message.setType(QuizMessage.MessageType.WIN);
        message.setWinner(winner);
        message.setPlayers(new ArrayList<>(players.values()));
        messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
        players.clear();
        playerQueue.clear();
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

    public QuizPlayer getCurrentPlayer() {
        return currentPlayer;
    }
    public String getLobbyId() {
        return lobbyId;
    }
}