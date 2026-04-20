package com.iatechnology.research_platform.service.impl;

import com.iatechnology.research_platform.entity.Publication;
import com.iatechnology.research_platform.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicationAsyncService {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private SummarizeService summarizeService;

    @Autowired
    private PublicationRepository publicationRepository;

    @Async
    @Transactional
    public void processAiMetadata(Long publicationId, byte[] pdfBytes) {
        try {
            // Note: You will need to adapt your PdfService methods to accept byte[] instead of MultipartFile
            String text = pdfService.extractTextFromBytes(pdfBytes);
            String summary = summarizeService.summarizeLargeText(text);

            try {
                List<byte[]> images = pdfService.extractImagesFromBytes(pdfBytes);
                List<String> imageSummaries = new ArrayList<>();

                if (images != null && !images.isEmpty()) {
                    for (byte[] img : images) {
                        String imgSummary = summarizeService.summarizeImage(img, null);
                        imageSummaries.add(imgSummary);
                    }
                }
                if (!imageSummaries.isEmpty()) {
                    summary += "\n\nImage Analysis:\n" + String.join("\n", imageSummaries);
                }
            } catch (Exception e) {
                System.out.println("Image extraction skipped: " + e.getMessage());
            }

            List<String> keywords = summarizeService.extractKeywords(summary);

            // Fetch the saved publication and update it with the AI results
            Publication publication = publicationRepository.findById(publicationId).orElse(null);
            if (publication != null) {
                publication.setResume(summary);
                publication.setMotsCles(keywords);
                publicationRepository.save(publication);
            }

        } catch (Exception e) {
            System.err.println("Error processing AI for publication " + publicationId + ": " + e.getMessage());
        }
    }
}