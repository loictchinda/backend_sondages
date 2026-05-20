package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.OptionReponse;
import com.example.demo.entity.Sondage;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.OptionReponseRepository;
import com.example.demo.repository.SondageRepository;
import com.example.demo.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SondageService {

    private final SondageRepository sondageRepository;
    private final OptionReponseRepository optionReponseRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Transactional
    public SondageResponse creerSondage(CreateSondageRequest request, String pseudo) {
        // 1. Récupérer le créateur via le pseudo du token JWT
        Utilisateur createur = utilisateurRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Créer le sondage
        Sondage sondage = Sondage.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .createur(createur)
                .build();

        sondage = sondageRepository.save(sondage);

        // 3. Créer les options liées au sondage
        final Sondage sondageSaved = sondage;
        List<OptionReponse> options = request.getOptions().stream()
                .map(opt -> OptionReponse.builder()
                        .texteOption(opt.getTexteOption())
                        .sondage(sondageSaved)
                        .build())
                .collect(Collectors.toList());

        optionReponseRepository.saveAll(options);
        sondageSaved.setOptions(options);

        // 4. Retourner la réponse
        return toResponse(sondageSaved);
    }

    // Convertir entité -> DTO réponse
    private SondageResponse toResponse(Sondage sondage) {
        List<OptionReponseResponse> optionsResponse = List.of();
        if (sondage.getOptions() != null) {
            optionsResponse = sondage.getOptions().stream()
                    .map(opt -> new OptionReponseResponse(
                            opt.getIdOption(),
                            opt.getTexteOption(),
                            0L // pas de votes à la création
                    ))
                    .collect(Collectors.toList());
        }

        return new SondageResponse(
                sondage.getIdSondage(),
                sondage.getTitre(),
                sondage.getDescription(),
                sondage.getTokenPublic(),
                sondage.getVisibilite().name(),
                sondage.getDateCreation(),
                sondage.getDateFin(),
                sondage.getCreateur().getPseudo(),
                0L, // pas de votes à la création
                optionsResponse
        );
    }
}