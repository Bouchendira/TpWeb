package com.iatechnology.research_platform.controller;

import com.iatechnology.research_platform.dto.PublicationRequest;
import com.iatechnology.research_platform.dto.PublicationResponse;
import com.iatechnology.research_platform.entity.StatutPublication;
import com.iatechnology.research_platform.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    // ── Public endpoints ─────────────────────────────────────────────────────

    // Returns PUBLISHED only — for homepage and public browsing
    @GetMapping
    public ResponseEntity<List<PublicationResponse>> getPublished() {
        return ResponseEntity.ok(publicationService.findPublished());
    }

    // Returns highlighted publications for homepage
    @GetMapping("/highlighted")
    public ResponseEntity<List<PublicationResponse>> getHighlighted() {
        return ResponseEntity.ok(publicationService.findHighlighted());
    }

    // Anyone can read a single publication
    @GetMapping("/{id}")
    public ResponseEntity<PublicationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.findById(id));
    }

    // ── Authenticated user endpoints ─────────────────────────────────────────

    // Spec: "Recherche par mots-clés"
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PublicationResponse>> searchByMotsCles(
            @RequestParam String q) {
        return ResponseEntity.ok(publicationService.searchByMotsCles(q));
    }

    // Spec: "Consultation des publications d'un chercheur"
    @GetMapping("/chercheur/{chercheurId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PublicationResponse>> getByChercheur(
            @PathVariable Long chercheurId) {
        return ResponseEntity.ok(publicationService.findByChercheur(chercheurId));
    }

    // Spec: "Recherche par domaine"
    @GetMapping("/domaine/{domaineId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PublicationResponse>> getByDomaine(
            @PathVariable Long domaineId) {
        return ResponseEntity.ok(publicationService.findByDomaine(domaineId));
    }

    // ── Admin only endpoints ──────────────────────────────────────────────────

    // Admin sees ALL publications including EN_ATTENTE and ARCHIVE
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PublicationResponse>> getAll() {
        return ResponseEntity.ok(publicationService.findAll());
    }

    // Spec: "Ajout" + "Association à un ou plusieurs chercheurs" + "Upload DOI"
    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicationResponse> create(
            @Valid  @ModelAttribute  PublicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(publicationService.create(request));
    }

    // Spec: "Modification"
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PublicationRequest request) {
        return ResponseEntity.ok(publicationService.update(id, request));
    }

    // Spec: "Suppression"
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Spec: publication workflow management EN_ATTENTE → PUBLIE → ARCHIVE
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicationResponse> changeStatut(
            @PathVariable Long id,
            @RequestParam StatutPublication statut) {
        return ResponseEntity.ok(publicationService.changeStatut(id, statut));
    }
}