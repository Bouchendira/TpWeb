package com.iatechnology.research_platform.dto;

import com.iatechnology.research_platform.entity.StatutPublication;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PublicationResponse {
    private Long id;
    private String titre;
    private String resume;
    private String motsCles;
    private String doi;
    private String fichierPdf;
    private LocalDate datePublication;
    private StatutPublication statut;

    // Flat chercheur info — no circular reference
    private List<ChercheurBriefDTO> chercheurs;

    @Data
    public static class ChercheurBriefDTO {
        private Long id;
        private String nom;
        private String prenom;
        private String domaineNom;
    }
}
