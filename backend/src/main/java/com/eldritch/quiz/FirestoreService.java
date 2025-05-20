package com.eldritch.quiz;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private Firestore db;

    @Autowired
    public FirestoreService(Firestore db) {
        this.db = db;
    }

    public void saveLobbyState(String lobbyId, Map<String, Object> state) {
        try {
            DocumentReference docRef = db.collection("quizLobbies").document(lobbyId);
            ApiFuture<WriteResult> result = docRef.set(state);
            result.get(); // Wait for operation to complete
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to save lobby state", e);
        }
    }

    public Map<String, Object> loadLobbyState(String lobbyId) {
        try {
            DocumentReference docRef = db.collection("quizLobbies").document(lobbyId);
            DocumentSnapshot document = docRef.get().get();
            if (document.exists()) {
                return document.getData();
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to load lobby state", e);
        }
    }

    public void deleteLobbyState(String lobbyId) {
        try {
            DocumentReference docRef = db.collection("quizLobbies").document(lobbyId);
            ApiFuture<WriteResult> result = docRef.delete();
            result.get(); // Wait for operation to complete
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete lobby state", e);
        }
    }

    public Firestore getDb() {
        return this.db;
    }
    public void deleteAllLobbies() {
        try {
            // Get all documents in the quizLobbies collection
            ApiFuture<QuerySnapshot> future = db.collection("quizLobbies").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            // Delete each document
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete all lobbies", e);
        }
    }
}