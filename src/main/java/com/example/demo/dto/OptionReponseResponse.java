package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionReponseResponse {

    private Long idOption;
    private String texteOption;
    private Long nombreVotes;
}