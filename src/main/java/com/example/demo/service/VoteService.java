package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ResultatSondageResponse;
import com.example.demo.dto.VoteRequest;
import com.example.demo.dto.HistoriqueVoteResponse;
import com.example.demo.entity.OptionReponse;
import com.example.demo.entity.Sondage;
import com.example.demo.entity.Utilisateur;
import com.example.demo.entity.Vote;
import com.example.demo.entity.InvitationId;
import com.example.demo.repository.OptionReponseRepository;
import com.example.demo.repository.SondageRepository;
import com.example.demo.repository.UtilisateurRepository;
import com.example.demo.repository.VoteRepository;
import com.example.demo.repository.InvitationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SondageRepository sondageRepository;
    private final OptionReponseRepository optionReponseRepository;
    private final InvitationRepository invitationRepository;
    
    @Transactional
    public void enregistrerVote(Long idSondage, VoteRequest request, String pseudo) {
        // 1. Récupérer l'utilisateur et le sondage
        Utilisateur utilisateur = utilisateurRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable."));

        // ====== 🔒 BLOCAGE MULTI-SALLE (Tâche 2.4) ======
        if (sondage.getVisibilite() == Sondage.Visibilite.prive) {
            boolean estLeCreateur = sondage.getCreateur().getPseudo().equals(pseudo);
            InvitationId idInvitation = new InvitationId(utilisateur.getIdUtilisateur(), sondage.getIdSondage());
            
            // Si l'utilisateur n'est pas le créateur ET qu'il n'est pas dans la table invitation -> Bloquer
            if (!estLeCreateur && !invitationRepository.existsById(idInvitation)) {
                throw new RuntimeException("Accès refusé : Ce sondage est privé et vous n'êtes pas invité.");
            }
        }
        // =================================================

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
    
    @Transactional(readOnly = true)
    public ResultatSondageResponse getResultats(Long idSondage) {
        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable."));

        long totalVotes = voteRepository.countBySondageIdSondage(idSondage);

        List<ResultatSondageResponse.ResultatOptionResponse> optionsResultat = sondage.getOptions().stream()
                .map(opt -> {
                    long nbVotes = voteRepository.countByOptionReponseIdOption(opt.getIdOption());
                    double pourcentage = totalVotes > 0 ? Math.round((double) nbVotes / totalVotes * 10000.0) / 100.0 : 0.0;
                    return new ResultatSondageResponse.ResultatOptionResponse(
                            opt.getIdOption(),
                            opt.getTexteOption(),
                            nbVotes,
                            pourcentage
                    );
                })
                .collect(Collectors.toList());

        return new ResultatSondageResponse(
                sondage.getIdSondage(),
                sondage.getTitre(),
                totalVotes,
                optionsResultat
        );
    }
    
    @Transactional(readOnly = true)
    public List<HistoriqueVoteResponse> getHistoriqueVotes(String pseudo) {
        Utilisateur utilisateur = utilisateurRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        List<Vote> votes = voteRepository.findByUtilisateurOrderByDateVoteDesc(utilisateur);

        return votes.stream()
                .map(vote -> new HistoriqueVoteResponse(
                        vote.getSondage().getIdSondage(),
                        vote.getSondage().getTitre(),
                        vote.getSondage().getTokenPublic(),
                        vote.getOptionReponse().getIdOption(),
                        vote.getOptionReponse().getTexteOption(),
                        vote.getDateVote()
                ))
                .collect(Collectors.toList());
    }
}