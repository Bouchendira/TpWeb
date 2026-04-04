package com.iatechnology.research_platform.service;

import com.iatechnology.research_platform.dto.ChercheurRequest;
import com.iatechnology.research_platform.dto.ChercheurResponse;
import java.util.List;

public interface ChercheurService {

    // Used by everyone — public access
    List<ChercheurResponse> findAll();
    ChercheurResponse findById(Long id);

    // Search use cases from the spec:
    // "Recherche par nom de chercheur" — accessible to authenticated users
    List<ChercheurResponse> searchByNom(String nom);

    // "Recherche par domaine" — accessible to authenticated users
    List<ChercheurResponse> findByDomaine(Long domaineId);

    // Admin only
    ChercheurResponse create(ChercheurRequest request);
    ChercheurResponse update(Long id, ChercheurRequest request);
    void delete(Long id);
}