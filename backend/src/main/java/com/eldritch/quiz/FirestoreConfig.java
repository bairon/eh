package com.eldritch.quiz;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("C:/Users/asak/IdeaProjects/eh/eldritch-horror-453809-abc57eeff771.json");

        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseId("ehdb")
                .build();

        Firestore db = firestoreOptions.getService();
        return db;
    }
}