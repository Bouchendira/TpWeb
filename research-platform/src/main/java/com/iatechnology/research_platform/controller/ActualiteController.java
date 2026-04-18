package com.iatechnology.research_platform.controller;

import com.iatechnology.research_platform.dto.ActualiteRequest;
import com.iatechnology.research_platform.dto.ActualiteResponse;
import com.iatechnology.research_platform.service.ActualiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actualites")
@RequiredArgsConstructor

public class ActualiteController {

    private final ActualiteService actualiteService;

    @PostMapping
    @PreAuthorize("hasRole('MODERATEUR') or hasRole('ADMIN')")
    public ResponseEntity<ActualiteResponse> createActualite(@RequestBody ActualiteRequest request) {
        ActualiteResponse response = actualiteService.createActualite(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ActualiteResponse>> getAllActualites() {
        List<ActualiteResponse> actualites = actualiteService.getAllActualites();
        return ResponseEntity.ok(actualites);
    }

    @GetMapping("/accueil")
    public ResponseEntity<List<ActualiteResponse>> getActualitesAccueil() {
        List<ActualiteResponse> actualites = actualiteService.getActualitesAccueil();
        return ResponseEntity.ok(actualites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActualiteResponse> getActualiteById(@PathVariable Long id) {
        ActualiteResponse response = actualiteService.getActualiteById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR') or hasRole('ADMIN')")
    public ResponseEntity<ActualiteResponse> updateActualite(@PathVariable Long id, @RequestBody ActualiteRequest request) {
        ActualiteResponse response = actualiteService.updateActualite(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATEUR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteActualite(@PathVariable Long id) {
        actualiteService.deleteActualite(id);
        return ResponseEntity.noContent().build();
    }
}