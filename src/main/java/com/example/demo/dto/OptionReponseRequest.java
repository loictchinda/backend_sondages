package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OptionReponseRequest {

    @NotBlank(message = "Le texte de l'option est obligatoire")
    @Size(max = 255, message = "Le texte ne doit pas dépasser 255 caractères")
    private String texteOption;
}