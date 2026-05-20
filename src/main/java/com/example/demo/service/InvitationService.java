package com.example.demo.service;

import com.example.demo.dto.InvitationRequest;
import com.example.demo.entity.Invitation;
import com.example.demo.entity.InvitationId;
import com.example.demo.entity.Sondage;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.InvitationRepository;
import com.example.demo.repository.SondageRepository;
import com.example.demo.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final SondageRepository sondageRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Transactional
    public void inviterUtilisateur(Long idSondage, InvitationRequest request, String pseudoCreateur) {
        // 1. Récupérer le sondage
        Sondage sondage = sondageRepository.findById(idSondage)
                .orElseThrow(() -> new RuntimeException("Sondage introuvable."));

        // 2. Sécurité : Seul le créateur peut inviter du monde
        if (!sondage.getCreateur().getPseudo().equals(pseudoCreateur)) {
            throw new RuntimeException("Action non autorisée : Vous n'êtes pas le créateur de ce sondage.");
        }

        // 3. Cohérence : On n'invite pas sur un sondage déjà public
        if (sondage.getVisibilite() != Sondage.Visibilite.prive) {
            throw new RuntimeException("Opération impossible : Ce sondage est public.");
        }

        // 4. Récupérer le profil de l'invité
        Utilisateur invite = utilisateurRepository.findByPseudo(request.getPseudoInvite())
                .orElseThrow(() -> new RuntimeException("L'utilisateur à inviter n'existe pas."));

        // 5. Éviter les doublons d'invitation
        InvitationId invitationId = new InvitationId(invite.getIdUtilisateur(), sondage.getIdSondage());
        if (invitationRepository.existsById(invitationId)) {
            throw new RuntimeException("Cet utilisateur est déjà invité à ce sondage.");
        }

        // 6. Enregistrer l'invitation
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .utilisateur(invite)
                .sondage(sondage)
                .build();

        invitationRepository.save(invitation);
    }
}