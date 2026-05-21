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
import com.example.demo.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SondageService {

    private final SondageRepository sondageRepository;
    private final OptionReponseRepository optionReponseRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public SondageResponse creerSondage(CreateSondageRequest request, String pseudo) {
        Utilisateur createur = utilisateurRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Sondage sondage = Sondage.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .createur(createur)
                .build();

        sondage = sondageRepository.save(sondage);

        final Sondage sondageSaved = sondage;
        List<OptionReponse> options = request.getOptions().stream()
                .map(opt -> OptionReponse.builder()
                        .texteOption(opt.getTexteOption())
                        .sondage(sondageSaved)
                        .build())
                .collect(Collectors.toList());

        optionReponseRepository.saveAll(options);
        sondageSaved.setOptions(options);

        return toResponse(sondageSaved);
    }
    
    public List<SondageResponse> getAllSondagesPublics() {
        return sondageRepository.findByVisibilite(Sondage.Visibilite.PUBLIC).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

   private SondageResponse toResponse(Sondage sondage) {
    List<OptionReponseResponse> optionsResponse = List.of();

    if (sondage.getOptions() != null) {
        optionsResponse = sondage.getOptions().stream()
                .map(opt -> {
                    long nbVotes = voteRepository.countByOptionReponseIdOption(opt.getIdOption());
                    return new OptionReponseResponse(
                            opt.getIdOption(),
                            opt.getTexteOption(),
                            nbVotes
                    );
                })
                .collect(Collectors.toList());
    }

   
    long totalVotes = optionsResponse.stream()
            .mapToLong(opt -> opt.getNombreVotes())   
            .sum();

    return new SondageResponse(
            sondage.getIdSondage(),
            sondage.getTitre(),
            sondage.getDescription(),
            sondage.getTokenPublic(),
            sondage.getVisibilite().name(),
            sondage.getDateCreation(),
            sondage.getDateFin(),
            sondage.getCreateur().getPseudo(),
            totalVotes,
            optionsResponse
    );
}
    @Transactional(readOnly = true)
    public SondageResponse consulterParToken(String tokenPublic) {
        Sondage sondage = sondageRepository.findByTokenPublic(tokenPublic)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable avec ce token."));
        return toResponse(sondage);
    }
    
    @Transactional
    public void supprimerSondage(Long idSondage, String pseudoUtilisateur) {
        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable avec cet ID."));

        if (!sondage.getCreateur().getPseudo().equals(pseudoUtilisateur)) {
            throw new RuntimeException("Action non autorisée : Vous n'êtes pas le créateur de ce sondage.");
        }

        sondageRepository.delete(sondage);
    }

    @Transactional
    public SondageResponse editerSondage(Long idSondage, CreateSondageRequest request, String pseudoUtilisateur) {
        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable avec cet ID."));

        if (!sondage.getCreateur().getPseudo().equals(pseudoUtilisateur)) {
            throw new RuntimeException("Action non autorisée : Vous n'êtes pas le créateur de ce sondage.");
        }

        boolean aDesVotes = sondage.getOptions().stream()
                .anyMatch(opt -> !opt.getVotes().isEmpty());
        if (aDesVotes) {
            throw new RuntimeException("Impossible de modifier un sondage qui a déjà reçu des votes.");
        }

        sondage.setTitre(request.getTitre());
        sondage.setDescription(request.getDescription());

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

    public List<SondageResponse> getSondagesByAuteur(String pseudo) {

        Utilisateur createur = utilisateurRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return sondageRepository.findByCreateur_IdUtilisateur(createur.getIdUtilisateur())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SondageResponse getByIdForUser(Long id, String pseudo) {
        Sondage sondage = sondageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable"));

        if (!sondage.getCreateur().getPseudo().equals(pseudo)) {
            throw new RuntimeException("Non autorisé");
        }

        return toResponse(sondage);
    }
}