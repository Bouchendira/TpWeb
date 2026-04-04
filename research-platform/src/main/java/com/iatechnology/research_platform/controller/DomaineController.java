package com.iatechnology.research_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iatechnology.research_platform.entity.Domaine;
import com.iatechnology.research_platform.service.DomaineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/domaines")
@CrossOrigin(origins = "http://localhost:4200")
public class DomaineController {

    @Autowired
    private DomaineService domaineService;

    @GetMapping
    public ResponseEntity<List<Domaine>> getAll() {
        return ResponseEntity.ok(domaineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Domaine> getById(@PathVariable Long id) {
        return domaineService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Domaine> create(@Valid @RequestBody Domaine domaine) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(domaineService.save(domaine));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Domaine> update(@PathVariable Long id,
                                           @Valid @RequestBody Domaine domaine) {
        return ResponseEntity.ok(domaineService.update(id, domaine));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        domaineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Domaine>> search(@RequestParam String nom) {
        return ResponseEntity.ok(domaineService.searchByNom(nom));
    }
}
