package com.iatechnology.research_platform.service.impl;

import com.iatechnology.research_platform.dto.ActualiteRequest;
import com.iatechnology.research_platform.dto.ActualiteResponse;
import com.iatechnology.research_platform.entity.Actualite;
import com.iatechnology.research_platform.repository.ActualiteRepository;
import com.iatechnology.research_platform.service.ActualiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActualiteServiceImpl implements ActualiteService {

    private final ActualiteRepository actualiteRepository;

    @Override
    public ActualiteResponse createActualite(ActualiteRequest request) {
        Actualite actualite = new Actualite();
        actualite.setTitre(request.getTitre());
        actualite.setContenu(request.getContenu());
        actualite.setAfficherAccueil(request.isAfficherAccueil());

        Actualite saved = actualiteRepository.save(actualite);
        return mapToResponse(saved);
    }

    @Override
    public List<ActualiteResponse> getAllActualites() {
        return actualiteRepository.findAllByOrderByDateCreationDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActualiteResponse> getActualitesAccueil() {
        return actualiteRepository.findByAfficherAccueilTrueOrderByDateCreationDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ActualiteResponse getActualiteById(Long id) {
        Actualite actualite = actualiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actualite not found with id: " + id));
        return mapToResponse(actualite);
    }

    @Override
    public ActualiteResponse updateActualite(Long id, ActualiteRequest request) {
        Actualite existing = actualiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actualite not found with id: " + id));

        existing.setTitre(request.getTitre());
        existing.setContenu(request.getContenu());
        existing.setAfficherAccueil(request.isAfficherAccueil());

        Actualite updated = actualiteRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void deleteActualite(Long id) {
        if (!actualiteRepository.existsById(id)) {
            throw new RuntimeException("Actualite not found with id: " + id);
        }
        actualiteRepository.deleteById(id);
    }

    private ActualiteResponse mapToResponse(Actualite actualite) {
        return new ActualiteResponse(
                actualite.getId(),
                actualite.getTitre(),
                actualite.getContenu(),
                actualite.getDateCreation(),
                actualite.isAfficherAccueil()
        );
    }
}