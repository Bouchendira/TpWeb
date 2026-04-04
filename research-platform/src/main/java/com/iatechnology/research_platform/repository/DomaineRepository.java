package com.iatechnology.research_platform.repository;
import com.iatechnology.research_platform.entity.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DomaineRepository extends JpaRepository<Domaine, Long> {
    Optional<Domaine> findByNom(String nom);
    List<Domaine> findByNomContainingIgnoreCase(String nom);
}
