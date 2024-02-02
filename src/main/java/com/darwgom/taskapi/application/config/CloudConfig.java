package com.darwgom.taskapi.application.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.io.BaseEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class CloudConfig {

    @Value("${spring.cloud.gcp.credentials.encoded-key}")
    private String encodedCredentials;

    @Bean
    public GoogleCredentials googleCloudCredentials() throws IOException {
        byte[] decodedKey = BaseEncoding.base64().decode(encodedCredentials);
        try (InputStream credentialsStream = new ByteArrayInputStream(decodedKey)) {
            return GoogleCredentials.fromStream(credentialsStream);
        }
    }

}
