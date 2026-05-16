//Firebase 설정
//FCM 푸시 알림 발송을 위한 Firebase 초기화

package com.maeum.maeum.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Value("${firebase.service-account}")
    private String serviceAccount;

    @PostConstruct
    public void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(serviceAccount).getInputStream())
                    .createScoped("https://www.googleapis.com/authfirebase.messaging");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials).build();

            FirebaseApp.initializeApp(options);
        }
    }
}
