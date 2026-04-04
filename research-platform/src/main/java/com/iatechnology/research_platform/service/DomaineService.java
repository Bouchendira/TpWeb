package com.iatechnology.research_platform.service;

import java.util.List;
import java.util.Optional;

import com.iatechnology.research_platform.entity.Domaine;

public interface DomaineService {
    List<Domaine> findAll();
    Optional<Domaine> findById(Long id);
    Domaine save(Domaine domaine);
    Domaine update(Long id, Domaine domaine);
    void deleteById(Long id);
    List<Domaine> searchByNom(String nom);
}
