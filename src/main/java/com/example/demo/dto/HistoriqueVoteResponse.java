package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HistoriqueVoteResponse {
    private Long idSondage;
    private String titreSondage;
    private String tokenPublic;
    private Long idOptionChoisie;
    private String texteOptionChoisie;
    private LocalDateTime dateVote;
}