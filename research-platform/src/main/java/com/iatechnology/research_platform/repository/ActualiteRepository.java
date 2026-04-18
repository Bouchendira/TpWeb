package com.iatechnology.research_platform.repository;

import com.iatechnology.research_platform.entity.Actualite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActualiteRepository extends JpaRepository<Actualite, Long> {
    List<Actualite> findByAfficherAccueilTrueOrderByDateCreationDesc();
    List<Actualite> findAllByOrderByDateCreationDesc();
}