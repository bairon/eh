package com.eldritch.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizMessage {
    private MessageType type;
    private String content;
    private QuizPlayer player;
    private QuizQuestion question;
    private Integer selectedAnswer;
    private Boolean correct;
    private QuizPlayer winner;
    private ArrayList<QuizPlayer> players;

    public enum MessageType {
        JOIN, CONNECTED, REJOIN, QUESTION, ANSWER, WIN, TERMINATE
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type != null ? type.name() : null);
        map.put("content", content);
        map.put("player", player != null ? player.toMap() : null);
        map.put("question", question != null ? question.toMap() : null);
        map.put("selectedAnswer", selectedAnswer);
        map.put("correct", correct);
        map.put("winner", winner != null ? winner.toMap() : null);

        if (players != null) {
            List<Map<String, Object>> playersList = new ArrayList<>();
            for (QuizPlayer p : players) {
                playersList.add(p.toMap());
            }
            map.put("players", playersList);
        }

        return map;
    }

    public static QuizMessage fromMap(Map<String, Object> map) {
        if (map == null) return null;

        QuizMessage message = new QuizMessage();
        message.setType(map.get("type") != null ? MessageType.valueOf((String) map.get("type")) : null);
        message.setContent((String) map.get("content"));

        if (map.get("player") != null) {
            message.setPlayer(QuizPlayer.fromMap((Map<String, Object>) map.get("player")));
        }

        if (map.get("question") != null) {
            message.setQuestion(QuizQuestion.fromMap((Map<String, Object>) map.get("question")));
        }

        message.setSelectedAnswer(map.get("selectedAnswer") != null ? ((Number) map.get("selectedAnswer")).intValue() : null);
        message.setCorrect((Boolean) map.get("correct"));

        if (map.get("winner") != null) {
            message.setWinner(QuizPlayer.fromMap((Map<String, Object>) map.get("winner")));
        }

        if (map.get("players") != null) {
            List<Map<String, Object>> playersList = (List<Map<String, Object>>) map.get("players");
            ArrayList<QuizPlayer> players = new ArrayList<>();
            for (Map<String, Object> playerMap : playersList) {
                players.add(QuizPlayer.fromMap(playerMap));
            }
            message.setPlayers(players);
        }

        return message;
    }
    public void setPlayers(ArrayList<QuizPlayer> players) {
        this.players = players;
    }

    public ArrayList<QuizPlayer> getPlayers() {
        return players;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuizPlayer getPlayer() {
        return player;
    }

    public void setPlayer(QuizPlayer player) {
        this.player = player;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public Integer getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(Integer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public QuizPlayer getWinner() {
        return winner;
    }

    public void setWinner(QuizPlayer winner) {
        this.winner = winner;
    }
}
