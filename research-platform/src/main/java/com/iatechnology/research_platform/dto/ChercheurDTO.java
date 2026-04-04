package com.iatechnology.research_platform.dto;

import lombok.Data;

@Data
public class ChercheurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String biographie;
    private String photo;
    private Long domaineId;
    private String domaineNom;
}
