package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UtilisateurResponse {
    
    private Long idUtilisateur;
    private String pseudo;
    private String email;
    
}