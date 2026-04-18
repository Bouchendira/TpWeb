package com.iatechnology.research_platform.controller;

import java.io.IOException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iatechnology.research_platform.service.impl.SupabaseStorageService;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final SupabaseStorageService storageService;

    public UploadController(SupabaseStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();

        return storageService.uploadFile(file, fileName);
    }
}
