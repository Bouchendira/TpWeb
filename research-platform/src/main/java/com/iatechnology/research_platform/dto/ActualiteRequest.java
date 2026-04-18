package com.iatechnology.research_platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ActualiteRequest {
    @NotBlank
    private String titre;

    @NotBlank
    private String contenu;

    private boolean afficherAccueil = false;
}