package com.iatechnology.research_platform.controller;

import com.iatechnology.research_platform.dto.ChercheurRequest;
import com.iatechnology.research_platform.dto.ChercheurResponse;
import com.iatechnology.research_platform.service.ChercheurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chercheurs")
@CrossOrigin(origins = "http://localhost:4200")
public class ChercheurController {

    @Autowired
    private ChercheurService chercheurService;

    // ── Public endpoints ─────────────────────────────────────────────────────

    // Anyone can browse the list of researchers
    @GetMapping
    public ResponseEntity<List<ChercheurResponse>> getAll() {
        return ResponseEntity.ok(chercheurService.findAll());
    }

    // Anyone can view a researcher's profile
    @GetMapping("/{id}")
    public ResponseEntity<ChercheurResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(chercheurService.findById(id));
    }

    // ── Authenticated user endpoints ─────────────────────────────────────────

    // Spec: "Recherche par nom de chercheur"
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChercheurResponse>> searchByNom(
            @RequestParam String nom) {
        return ResponseEntity.ok(chercheurService.searchByNom(nom));
    }

    // Spec: "Recherche par domaine"
    @GetMapping("/domaine/{domaineId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChercheurResponse>> getByDomaine(
            @PathVariable Long domaineId) {
        return ResponseEntity.ok(chercheurService.findByDomaine(domaineId));
    }

    // ── Admin only endpoints ──────────────────────────────────────────────────

    // Spec: "Ajouter un chercheur"
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChercheurResponse> create(
            @Valid @RequestBody ChercheurRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(chercheurService.create(request));
    }

    // Spec: "Modifier un chercheur"
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChercheurResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ChercheurRequest request) {
        return ResponseEntity.ok(chercheurService.update(id, request));
    }

    // Spec: "Supprimer un chercheur"
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chercheurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}