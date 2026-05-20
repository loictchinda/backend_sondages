package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SondageResponse {

    private Long idSondage;
    private String titre;
    private String description;
    private String tokenPublic;
    private String visibilite;
    private LocalDateTime dateCreation;
    private LocalDateTime dateFin;
    private String pseudoCreateur;
    private Long totalVotes;
    private List<OptionReponseResponse> options;
}