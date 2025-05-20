package com.eldritch.quiz;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Repository
public class QuizLobbyRepository {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "quiz_lobbies";

    public QuizLobbyRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    // Сохранить лобби
    public String save(QuizLobby lobby) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(lobby.getId());
        ApiFuture<WriteResult> result = docRef.set(lobby);
        result.get(); // Дожидаемся завершения операции
        return lobby.getId();
    }

    // Найти лобби по ID
    public Optional<QuizLobby> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return Optional.of(document.toObject(QuizLobby.class));
        }
        return Optional.empty();
    }

    // Обновить состояние лобби
    public void update(QuizLobby lobby) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(lobby.getId());
        ApiFuture<WriteResult> result = docRef.set(lobby, SetOptions.merge());
        result.get();
    }

    // Удалить лобби
    public void delete(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> result = firestore.collection(COLLECTION_NAME).document(id).delete();
        result.get();
    }

    // Найти все активные лобби
    public List<QuizLobby> findAllActive() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("active", true)
                .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<QuizLobby> lobbies = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            lobbies.add(document.toObject(QuizLobby.class));
        }
        
        return lobbies;
    }

    // Подписка на изменения лобби (режим реального времени)
    public void listenToLobbyChanges(String lobbyId, Consumer<QuizLobby> callback) {
        firestore.collection(COLLECTION_NAME).document(lobbyId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        System.err.println("Listen failed: " + e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        callback.accept(snapshot.toObject(QuizLobby.class));
                    }
                });
    }
}