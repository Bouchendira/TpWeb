package com.iatechnology.research_platform.repository;
import com.iatechnology.research_platform.entity.Publication;
import com.iatechnology.research_platform.entity.StatutPublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByTitreContainingIgnoreCase(String titre);
    List<Publication> findByMotsClesContainingIgnoreCase(String motsCle);
    List<Publication> findByChercheurs_Id(Long chercheurId);
    List<Publication> findByStatut(StatutPublication statut);
}
