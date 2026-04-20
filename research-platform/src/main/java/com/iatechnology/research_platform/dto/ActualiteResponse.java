package com.iatechnology.research_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ActualiteResponse {
    private Long id;
    private String titre;
    private String contenu;
    private LocalDateTime dateCreation;
    private boolean afficherAccueil;
}