package com.iatechnology.research_platform.service;

import com.iatechnology.research_platform.dto.ActualiteRequest;
import com.iatechnology.research_platform.dto.ActualiteResponse;

import java.util.List;

public interface ActualiteService {
    ActualiteResponse createActualite(ActualiteRequest request);
    List<ActualiteResponse> getAllActualites();
    List<ActualiteResponse> getActualitesAccueil();
    ActualiteResponse getActualiteById(Long id);
    ActualiteResponse updateActualite(Long id, ActualiteRequest request);
    void deleteActualite(Long id);
}