package com.iatechnology.research_platform.repository;

import com.iatechnology.research_platform.entity.Publication;
import com.iatechnology.research_platform.entity.StatutPublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    // Used by findPublished
    List<Publication> findByStatut(StatutPublication statut);

    // Used by searchByMotsCles — searches titre, motsCles AND resume
    List<Publication> findByTitreContainingIgnoreCaseOrMotsClesContainingIgnoreCaseOrResumeContainingIgnoreCase(
        String titre, String motsCles, String resume);

    // Used by findByChercheur
    List<Publication> findByChercheurs_Id(Long chercheurId);

    // Used by findByDomaine — navigates Publication → Chercheur → Domaine
    List<Publication> findByChercheurs_Domaine_Id(Long domaineId);
}