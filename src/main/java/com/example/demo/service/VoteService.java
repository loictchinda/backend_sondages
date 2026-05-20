package com.example.demo.service;

import com.example.demo.dto.VoteRequest;
import com.example.demo.entity.OptionReponse;
import com.example.demo.entity.Sondage;
import com.example.demo.entity.Utilisateur;
import com.example.demo.entity.Vote;
import com.example.demo.repository.OptionReponseRepository;
import com.example.demo.repository.SondageRepository;
import com.example.demo.repository.UtilisateurRepository;
import com.example.demo.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SondageRepository sondageRepository;
    private final OptionReponseRepository optionReponseRepository;

    @Transactional
    public void enregistrerVote(Long idSondage, VoteRequest request, String pseudo) {
        // 1. Récupérer l'utilisateur et le sondage
        Utilisateur utilisateur = utilisateurRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable."));

        // 2. Vérification métier : L'utilisateur a-t-il déjà voté ?
        if (voteRepository.existsByUtilisateurAndSondage(utilisateur, sondage)) {
            throw new RuntimeException("Erreur : Vous avez déjà voté pour ce sondage.");
        }

        // 3. Vérifier que l'option existe et appartient bien à CE sondage
        OptionReponse option = optionReponseRepository.findById(request.getIdOption())
                .orElseThrow(() -> new RuntimeException("Option de réponse introuvable."));

        if (!option.getSondage().getIdSondage().equals(sondage.getIdSondage())) {
            throw new RuntimeException("Erreur : Cette option n'appartient pas à ce sondage.");
        }

        // 4. Créer et sauvegarder le vote
        Vote vote = Vote.builder()
                .utilisateur(utilisateur)
                .sondage(sondage)
                .optionReponse(option)
                .build();

        try {
            voteRepository.save(vote);
        } catch (DataIntegrityViolationException e) {
            // Capture de la contrainte unique SQL (uk_utilisateur_sondage) si la vérification métier a été contournée
            throw new RuntimeException("Erreur de base de données : Vous avez déjà voté pour ce sondage.");
        }
    }
}