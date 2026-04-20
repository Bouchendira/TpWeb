package com.iatechnology.research_platform.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleRequest {
    private Set<String> roles;
}