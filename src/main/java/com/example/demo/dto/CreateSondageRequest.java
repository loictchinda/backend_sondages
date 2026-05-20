package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateSondageRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String titre;

    private String description;

    @NotEmpty(message = "Il faut au moins 2 options")
    @Size(min = 2, message = "Il faut au moins 2 options de réponse")
    private List<OptionReponseRequest> options;
}