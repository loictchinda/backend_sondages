package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InvitationRequest {
    @NotBlank(message = "Le pseudo de l'utilisateur à inviter est obligatoire")
    private String pseudoInvite;
}