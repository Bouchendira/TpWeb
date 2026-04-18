package com.iatechnology.research_platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Data
public class PublicationRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String resume;

    private List<String> motsCles;

    private String doi;

    private MultipartFile fichierPdf;

    private LocalDate datePublication;

    private boolean mettreEnAvant = false;

    private List<Long> chercheurIds;
}