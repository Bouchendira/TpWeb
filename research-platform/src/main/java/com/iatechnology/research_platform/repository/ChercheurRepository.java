package com.iatechnology.research_platform.repository;
import com.iatechnology.research_platform.entity.Chercheur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChercheurRepository extends JpaRepository<Chercheur, Long> {
    List<Chercheur> findByNomContainingIgnoreCase(String nom);
    List<Chercheur> findByDomaineId(Long domaineId);
}
