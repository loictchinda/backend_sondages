package com.example.demo.service;

import com.example.demo.dto.UtilisateurResponse;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public List<UtilisateurResponse> getAllUsers() {
        return utilisateurRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private UtilisateurResponse toResponse(Utilisateur utilisateur) {
        return new UtilisateurResponse(
                utilisateur.getIdUtilisateur(),
                utilisateur.getPseudo(),
                utilisateur.getEmail()
        );
    }
}