package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(max = 100)
    private String nom;
    
    @NotBlank @Size(max = 100)
    private String prenom;
    
    @NotBlank @Size(min = 3, max = 50)
    private String pseudo;
    
    @NotBlank @Email @Size(max = 150)
    private String email;
    
    private String telephone;
    
    @NotBlank @Size(min = 6, max = 255)
    private String motDePasse;
}