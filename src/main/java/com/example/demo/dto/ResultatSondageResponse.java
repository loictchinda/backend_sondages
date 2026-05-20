package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResultatSondageResponse {

    private Long idSondage;
    private String titre;
    private Long totalVotes;
    private List<ResultatOptionResponse> options;

    @Data
    @AllArgsConstructor
    public static class ResultatOptionResponse {
        private Long idOption;
        private String texteOption;
        private Long nombreVotes;
        private Double pourcentage;
    }
}