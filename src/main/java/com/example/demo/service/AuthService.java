package com.example.demo.service;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;
import com.example.demo.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public String register(RegisterRequest request) {
        if (utilisateurRepository.existsByPseudo(request.getPseudo())) {
            throw new RuntimeException("Erreur: Ce pseudo est deja pris !");
        }
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Erreur: Cet email est deja utilise !");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .pseudo(request.getPseudo())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .build();

        utilisateurRepository.save(utilisateur);
        return "Utilisateur inscrit avec succes !";
    }

    public JwtResponse login(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByPseudo(request.getPseudo())
                .orElseThrow(() -> new RuntimeException("Erreur: Pseudo ou mot de passe incorrect."));

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Erreur: Pseudo ou mot de passe incorrect.");
        }

        String token = jwtUtils.generateJwtToken(utilisateur.getPseudo());
        return new JwtResponse(token, utilisateur.getPseudo(), utilisateur.getEmail());
    }
}