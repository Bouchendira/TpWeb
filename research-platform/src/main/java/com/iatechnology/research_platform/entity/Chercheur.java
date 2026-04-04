package com.iatechnology.research_platform.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Entity
@Table(name = "chercheurs")
@Data
public class Chercheur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String prenom;
    private String email;
    private String biographie;
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domaine_id")
    private Domaine domaine;

    @ManyToMany(mappedBy = "chercheurs")
    private List<Publication> publications;
}
