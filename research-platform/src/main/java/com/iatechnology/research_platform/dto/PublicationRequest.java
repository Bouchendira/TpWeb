package com.iatechnology.research_platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PublicationRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String resume;
    private String motsCles;
    private String doi;
    private String fichierPdf;
    private LocalDate datePublication;

    // List of chercheur IDs to associate — at least one required
    private List<Long> chercheurIds;
}