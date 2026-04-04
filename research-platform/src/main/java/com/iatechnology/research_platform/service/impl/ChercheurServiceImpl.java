package com.iatechnology.research_platform.service.impl;

import com.iatechnology.research_platform.dto.ChercheurRequest;
import com.iatechnology.research_platform.dto.ChercheurResponse;
import com.iatechnology.research_platform.entity.Chercheur;
import com.iatechnology.research_platform.entity.Domaine;
import com.iatechnology.research_platform.repository.ChercheurRepository;
import com.iatechnology.research_platform.repository.DomaineRepository;
import com.iatechnology.research_platform.service.ChercheurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChercheurServiceImpl implements ChercheurService {

    @Autowired
    private ChercheurRepository chercheurRepository;

    @Autowired
    private DomaineRepository domaineRepository;

    // ─── Mapping helpers ────────────────────────────────────────────────────

    private ChercheurResponse toResponse(Chercheur c) {
        ChercheurResponse dto = new ChercheurResponse();
        dto.setId(c.getId());
        dto.setNom(c.getNom());
        dto.setPrenom(c.getPrenom());
        dto.setEmail(c.getEmail());
        dto.setBiographie(c.getBiographie());
        dto.setPhoto(c.getPhoto());

        if (c.getDomaine() != null) {
            dto.setDomaineId(c.getDomaine().getId());
            dto.setDomaineNom(c.getDomaine().getNom());
        }

        // Count publications without loading them all
        dto.setNombrePublications(
            c.getPublications() != null ? c.getPublications().size() : 0
        );

        return dto;
    }

    private Chercheur toEntity(ChercheurRequest request) {
        Chercheur chercheur = new Chercheur();
        chercheur.setNom(request.getNom());
        chercheur.setPrenom(request.getPrenom());
        chercheur.setEmail(request.getEmail());
        chercheur.setBiographie(request.getBiographie());
        chercheur.setPhoto(request.getPhoto());

        if (request.getDomaineId() != null) {
            Domaine domaine = domaineRepository.findById(request.getDomaineId())
                .orElseThrow(() -> new RuntimeException(
                    "Domaine non trouvé avec l'id: " + request.getDomaineId()));
            chercheur.setDomaine(domaine);
        }

        return chercheur;
    }

    // ─── Public use cases ───────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ChercheurResponse> findAll() {
        return chercheurRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ChercheurResponse findById(Long id) {
        Chercheur chercheur = chercheurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Chercheur non trouvé avec l'id: " + id));
        return toResponse(chercheur);
    }

    // Spec: "Recherche par nom de chercheur"
    @Override
    @Transactional(readOnly = true)
    public List<ChercheurResponse> searchByNom(String nom) {
        return chercheurRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, nom)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    // Spec: "Recherche par domaine"
    @Override
    @Transactional(readOnly = true)
    public List<ChercheurResponse> findByDomaine(Long domaineId) {
        // Verify domaine exists first
        domaineRepository.findById(domaineId)
            .orElseThrow(() -> new RuntimeException(
                "Domaine non trouvé avec l'id: " + domaineId));

        return chercheurRepository.findByDomaineId(domaineId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    // ─── Admin only use cases ────────────────────────────────────────────────

    @Override
    public ChercheurResponse create(ChercheurRequest request) {
        Chercheur chercheur = toEntity(request);
        return toResponse(chercheurRepository.save(chercheur));
    }

    @Override
    public ChercheurResponse update(Long id, ChercheurRequest request) {
        Chercheur chercheur = chercheurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Chercheur non trouvé avec l'id: " + id));

        // Update only fields that were sent
        chercheur.setNom(request.getNom());
        chercheur.setPrenom(request.getPrenom());
        chercheur.setEmail(request.getEmail());
        chercheur.setBiographie(request.getBiographie());
        chercheur.setPhoto(request.getPhoto());

        if (request.getDomaineId() != null) {
            Domaine domaine = domaineRepository.findById(request.getDomaineId())
                .orElseThrow(() -> new RuntimeException(
                    "Domaine non trouvé avec l'id: " + request.getDomaineId()));
            chercheur.setDomaine(domaine);
        }

        return toResponse(chercheurRepository.save(chercheur));
    }

    @Override
    public void delete(Long id) {
        if (!chercheurRepository.existsById(id)) {
            throw new RuntimeException("Chercheur non trouvé avec l'id: " + id);
        }
        chercheurRepository.deleteById(id);
    }
}