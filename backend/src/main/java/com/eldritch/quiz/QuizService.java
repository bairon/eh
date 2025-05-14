package com.eldritch.quiz;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class QuizService {
    public static final int QUIZ_WIN_SCORE = 5;
    private static final int QUIZ_PLAYERS_REQUIRED = 1;

    private final ConcurrentMap<String, QuizPlayer> players = new ConcurrentHashMap<>();
    private final BlockingQueue<QuizPlayer> playerQueue = new LinkedBlockingQueue<>();
    private QuizPlayer currentPlayer;
    private boolean quizRunning = false;
    private final SimpMessagingTemplate messagingTemplate;
    private final String lobbyId;


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
    private Thread quizThread = null;

    public QuizService(SimpMessagingTemplate messagingTemplate, String lobbyId) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyId = lobbyId;
    }

    @PostConstruct
    public void init() {
        System.out.println("QuizService initialized");
    }

    public synchronized QuizPlayer addPlayer(String nickname) {
        String id = UUID.randomUUID().toString();
        QuizPlayer player = new QuizPlayer(id, nickname, 0, true); // active by default
        players.put(id, player);
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

    // Add to class fields
    private QuizQuestion currentQuestion;

    // Modify startQuiz() method:

    public void startQuiz() {
        System.out.println("Starting Quiz");
        quizRunning = true;

        new Thread(() -> {
            try {
                while (quizRunning) {
                    currentPlayer = playerQueue.take();
                    currentPlayer.setActive(true);
                    currentQuestion = getRandomQuestion(); // Store the current question
                    sendPlayerUpdate();
                    sendQuestion(currentPlayer, currentQuestion); // Send the stored question

                    // Wait for answer
                    synchronized (currentPlayer) {
                        currentPlayer.wait(10000);
                    }

                    currentPlayer.setActive(false);
                    playerQueue.add(currentPlayer);

                    if (currentPlayer.getScore() >= QUIZ_WIN_SCORE) {
                        endQuiz(currentPlayer);
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public void quizCycle() {

    }

    // Add this class field to track answered players
    private final Set<String> answeredPlayers = Collections.synchronizedSet(new HashSet<>());

    public synchronized void processAnswer(String playerId, int answerIndex) {
        System.out.println("Process answer: " + answerIndex);
        System.out.println("Current Question: " + currentQuestion);
        if (!quizRunning || currentPlayer == null || !currentPlayer.getId().equals(playerId)) {
            return;
        }

        // Prevent multiple answers from same player
        if (answeredPlayers.contains(playerId)) {
            return;
        }

        // Add null check for currentQuestion
        if (currentQuestion == null) {
            return;
        }

        answeredPlayers.add(playerId);

        boolean isCorrect = answerIndex == currentQuestion.getCorrectOption();

        if (isCorrect) {
            currentPlayer.setScore(currentPlayer.getScore() + 1);
            // Check for win condition immediately after updating score
            if (currentPlayer.getScore() >= QUIZ_WIN_SCORE) {
                endQuiz(currentPlayer);
                return;  // Exit early since quiz has ended
            }
        }

        // Broadcast the answer to all players
        sendAnswerResult(currentPlayer, answerIndex, isCorrect, currentQuestion);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                synchronized (currentPlayer) {
                    answeredPlayers.remove(playerId);
                    currentPlayer.notify();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void sendPlayerUpdate() {
        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.UPDATE);
        message.setPlayers(new ArrayList<>(players.values()));
        messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
    }

    private void sendQuestion(QuizPlayer player, QuizQuestion question) {
        System.out.println("Sending question " + question + " to a player " + player);
        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.QUESTION);
        message.setPlayer(player);
        message.setQuestion(question);
        messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
    }

    private void sendAnswerResult(QuizPlayer player, int answerIndex, boolean isCorrect, QuizQuestion question) {
        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.ANSWER);
        message.setPlayer(player);
        message.setSelectedAnswer(answerIndex);
        message.setCorrect(isCorrect);
        message.setQuestion(question);
        message.setPlayers(new ArrayList<>(players.values()));
        messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId, message);
    }

    public void endQuiz(QuizPlayer winner) {
        quizRunning = false;
        QuizMessage message = new QuizMessage();
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