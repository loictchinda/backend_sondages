package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreateSondageRequest;
import com.example.demo.dto.OptionReponseResponse;
import com.example.demo.dto.SondageResponse;
import com.example.demo.entity.OptionReponse;
import com.example.demo.entity.Sondage;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.OptionReponseRepository;
import com.example.demo.repository.SondageRepository;
import com.example.demo.repository.UtilisateurRepository;

import lombok.RequiredArgsConstructor;

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
    
    public List<SondageResponse> getAllSondagesPublics() {
        return sondageRepository.findByVisibilite(Sondage.Visibilite.public_enum).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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
    
 // ... tes autres méthodes (creerSondage)

    @Transactional(readOnly = true)
    public SondageResponse consulterParToken(String tokenPublic) {
        // 1. Chercher le sondage par son token unique
        Sondage sondage = sondageRepository.findByTokenPublic(tokenPublic)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable avec ce token."));

        // 2. Utiliser la méthode existante pour convertir l'entité en DTO
        return toResponse(sondage);
    }
    
 // ... tes autres méthodes (creerSondage, consulterParToken, getAllSondagesPublics)

    @Transactional
    public void supprimerSondage(Long idSondage, String pseudoUtilisateur) {
        // 1. Chercher le sondage par son ID
        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable avec cet ID."));

        // 2. Vérification des droits : est-ce que le créateur est bien l'utilisateur connecté ?
        if (!sondage.getCreateur().getPseudo().equals(pseudoUtilisateur)) {
            throw new RuntimeException("Action non autorisée : Vous n'êtes pas le créateur de ce sondage.");
        }

        // 3. Suppression (Hibernate gérera la suppression en cascade des options grâce au CascadeType.ALL défini dans l'entité)
        sondageRepository.delete(sondage);
    }
    @Transactional
    public SondageResponse editerSondage(Long idSondage, CreateSondageRequest request, String pseudoUtilisateur) {
        // 1. Chercher le sondage
        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable avec cet ID."));

        // 2. Vérifier que c'est le créateur
        if (!sondage.getCreateur().getPseudo().equals(pseudoUtilisateur)) {
            throw new RuntimeException("Action non autorisée : Vous n'êtes pas le créateur de ce sondage.");
        }

        // 3. Vérifier qu'il n'y a pas encore de votes
        boolean aDesVotes = sondage.getOptions().stream()
                .anyMatch(opt -> !opt.getVotes().isEmpty());
        if (aDesVotes) {
            throw new RuntimeException("Impossible de modifier un sondage qui a déjà reçu des votes.");
        }

        // 4. Mettre à jour titre et description
        sondage.setTitre(request.getTitre());
        sondage.setDescription(request.getDescription());

        // 5. Supprimer les anciennes options et créer les nouvelles
        sondage.getOptions().clear();
        List<OptionReponse> nouvellesOptions = request.getOptions().stream()
                .map(opt -> OptionReponse.builder()
                        .texteOption(opt.getTexteOption())
                        .sondage(sondage)
                        .build())
                .collect(Collectors.toList());
        sondage.getOptions().addAll(nouvellesOptions);

        sondageRepository.save(sondage);
        return toResponse(sondage);
    }
}