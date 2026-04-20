package com.iatechnology.research_platform.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    // =========================
    // 1. TEXT EXTRACTION
    // =========================
    
    // Original method: Now just delegates to the byte[] version
    public String extractText(MultipartFile file) {
        try {
            return extractTextFromBytes(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier PDF", e);
        }
    }

    // NEW METHOD for the async background thread
    public String extractTextFromBytes(byte[] pdfBytes) {
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Erreur extraction PDF (texte)", e);
        }
    }

    // =========================
    // 2. IMAGE EXTRACTION (PAGE RENDERING)
    // =========================
    
    // Original method: Now just delegates to the byte[] version
    public List<byte[]> extractImages(MultipartFile pdfFile) {
        try {
            return extractImagesFromBytes(pdfFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier PDF", e);
        }
    }

    // NEW METHOD for the async background thread
    public List<byte[]> extractImagesFromBytes(byte[] pdfBytes) {
        List<byte[]> images = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 200);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);

                images.add(baos.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur extraction images PDF", e);
        }

        return images;
    }
}