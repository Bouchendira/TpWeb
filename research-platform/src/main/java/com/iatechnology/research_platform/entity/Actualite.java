package com.iatechnology.research_platform.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "actualites")
@Data
public class Actualite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateCreation = LocalDateTime.now();
    private boolean afficherAccueil = false;
}