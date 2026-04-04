package com.iatechnology.research_platform.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "publications")
@Data
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String resume;

    private String motsCles;
    private String doi;
    private String fichierPdf;
    private LocalDate datePublication;

    @Enumerated(EnumType.STRING)
    private StatutPublication statut = StatutPublication.EN_ATTENTE;

    @ManyToMany
    @JoinTable(name = "publication_chercheurs",
        joinColumns = @JoinColumn(name = "publication_id"),
        inverseJoinColumns = @JoinColumn(name = "chercheur_id"))
    private List<Chercheur> chercheurs;
}
