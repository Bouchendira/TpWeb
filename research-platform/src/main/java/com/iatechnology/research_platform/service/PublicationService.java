package com.iatechnology.research_platform.service;

import com.iatechnology.research_platform.dto.PublicationRequest;
import com.iatechnology.research_platform.dto.PublicationResponse;
import com.iatechnology.research_platform.entity.StatutPublication;
import java.util.List;

public interface PublicationService {

    // Public access
    List<PublicationResponse> findAll();
    List<PublicationResponse> findPublished();
    PublicationResponse findById(Long id);

    // Spec: "Recherche par mots-clés"
    List<PublicationResponse> searchByMotsCles(String motsCles);

    // Spec: "Recherche par nom de chercheur" — find publications of a researcher
    List<PublicationResponse> findByChercheur(Long chercheurId);

    // Spec: "Recherche par domaine" — find publications in a research domain
    List<PublicationResponse> findByDomaine(Long domaineId);

    // Admin only — spec: "Ajout, modification et suppression"
    PublicationResponse create(PublicationRequest request);
    PublicationResponse update(Long id, PublicationRequest request);
    void delete(Long id);

    // Admin only — spec: manages publication status
    PublicationResponse changeStatut(Long id, StatutPublication statut);
}