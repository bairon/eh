package com.eldritch.quiz;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class QuizPlayerRepository {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "quizPlayers";

    public QuizPlayerRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public String save(QuizPlayer player) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
            .document(player.getId())
            .set(player)
            .get()
            .getUpdateTime()
            .toString();
    }

    public QuizPlayer findById(String id) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
            .document(id)
            .get()
            .get()
            .toObject(QuizPlayer.class);
    }
    
    // Другие методы
}