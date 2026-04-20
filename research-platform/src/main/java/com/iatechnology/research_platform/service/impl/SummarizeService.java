package com.iatechnology.research_platform.service.impl;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class SummarizeService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // =======================
    // 1. SUMMARY
    // =======================
    public String summarize(String text) {

        String prompt =
                "Summarize this scientific document in 10 lines:\n\n" + text;

        return callGroq(prompt);
    }

    // =======================
    // 2. KEYWORDS
    // =======================
    public List<String> extractKeywords(String text) {

        String prompt =
                "Extract 5 to 10 academic keywords. Return only comma-separated words:\n\n" + text;

        String response = callGroq(prompt);

        return Arrays.stream(response.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
public String summarizeImage(byte[] imageBytes, String filename) {

    try {
        String apiUrl = "http://127.0.0.1:8000/analyze";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Build multipart body
        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("file", new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return filename != null ? filename : "image.jpg";
            }
        });

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(apiUrl, request, Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null || !responseBody.containsKey("generated_text")) {
            return "No description generated";
        }

        return responseBody.get("generated_text").toString();

    } catch (Exception e) {
        throw new RuntimeException("Erreur image AI: " + e.getMessage(), e);
    }
}
    // =======================
    // CORE CALL GROQ
    // =======================
    private String callGroq(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");

        body.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        body.put("temperature", 0.3);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(apiUrl, request, Map.class);

        Map resBody = response.getBody();

        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) resBody.get("choices");

        Map<String, Object> message =
                (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString().trim();
    }

    public List<String> splitText(String text, int maxSize) {

    List<String> chunks = new ArrayList<>();

    if (text == null || text.isEmpty()) {
        return chunks;
    }

    int i = 0;
    while (i < text.length()) {
        int end = Math.min(text.length(), i + maxSize);
        chunks.add(text.substring(i, end));
        i = end;
    }

    return chunks;
    }

    public String summarizeLargeText(String text) {

    List<String> chunks = splitText(text, 3000);

    StringBuilder intermediateSummaries = new StringBuilder();

    for (String chunk : chunks) {

        String summary = summarize(chunk);

        intermediateSummaries.append(summary).append("\n");

        // IMPORTANT pour éviter 429
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // STEP 2 : résumé final
    return summarize(intermediateSummaries.toString());

    }
   
    public List<String> extractKeywordsLargeText(String text) {

    List<String> chunks = splitText(text, 3000);

    Set<String> allKeywords = new HashSet<>();

    for (String chunk : chunks) {

        String response = callGroq(
                "Extract academic keywords (comma separated):\n\n" + chunk
        );

        Arrays.stream(response.split(","))
                .map(String::trim)
                .forEach(allKeywords::add);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    return new ArrayList<>(allKeywords);
}
}