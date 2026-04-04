package com.iatechnology.research_platform.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "domaines")
@Data
public class Domaine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String description;

    @OneToMany(mappedBy = "domaine", cascade = CascadeType.ALL)
    private List<Chercheur> chercheurs;
}