package com.iatechnology.research_platform.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import com.iatechnology.research_platform.entity.StatutPublication;
@Data
public class PublicationDTO {
    private Long id;
    private String titre;
    private String resume;
    private List<String> motsCles;
    private String doi;
    private String fichierPdf;
    private LocalDate datePublication;
    private StatutPublication statut;
    private List<Long> chercheurIds;
}
