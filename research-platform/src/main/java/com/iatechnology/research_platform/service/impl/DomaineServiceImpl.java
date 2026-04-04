package com.iatechnology.research_platform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iatechnology.research_platform.entity.Domaine;
import com.iatechnology.research_platform.repository.DomaineRepository;
import com.iatechnology.research_platform.service.DomaineService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DomaineServiceImpl implements DomaineService {

    @Autowired
    private DomaineRepository domaineRepository;

    @Override
    public List<Domaine> findAll() {
        return domaineRepository.findAll();
    }

    @Override
    public Optional<Domaine> findById(Long id) {
        return domaineRepository.findById(id);
    }

    @Override
    public Domaine save(Domaine domaine) {
        return domaineRepository.save(domaine);
    }

    @Override
    public Domaine update(Long id, Domaine domaineDetails) {
        Domaine domaine = domaineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Domaine non trouvé: " + id));
        domaine.setNom(domaineDetails.getNom());
        domaine.setDescription(domaineDetails.getDescription());
        return domaineRepository.save(domaine);
    }

    @Override
    public void deleteById(Long id) {
        domaineRepository.deleteById(id);
    }

    @Override
    public List<Domaine> searchByNom(String nom) {
        return domaineRepository.findByNomContainingIgnoreCase(nom);
    }
}
