package com.iatechnology.research_platform.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
public class SupabaseStorageService {

    private final WebClient webClient;

    private final String SUPABASE_URL = "https://xxprwjsagxvhinrozjqd.supabase.co";
    private final String API_KEY = "sb_secret_5jj2slOp1volYjsRg-N7KA_Bisi2Plt";
    private final String BUCKET = "storage";

    public SupabaseStorageService() {
        this.webClient = WebClient.builder()
                .baseUrl(SUPABASE_URL)
                .defaultHeader("apikey", API_KEY)
                .defaultHeader("Authorization", "Bearer " + API_KEY)
                .build();
    }

  public String uploadFile(MultipartFile file, String doi) throws IOException {

        String cleanFileName = sanitize(doi)+file.getOriginalFilename();

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/storage/v1/object/{bucket}/{file}")
                        .build(BUCKET, cleanFileName)
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(file.getBytes())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return getPublicUrl(cleanFileName);
    }

    private String sanitize(String fileName) {
        return fileName == null ? "file.pdf"
                : fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    public String getPublicUrl(String fileName) {
        return SUPABASE_URL +
                "/storage/v1/object/public/" +
                BUCKET + "/" +
                fileName;
    }
}
