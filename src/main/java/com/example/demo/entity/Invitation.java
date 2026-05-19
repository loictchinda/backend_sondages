package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation {

    @EmbeddedId
    private InvitationId id;

    @Builder.Default
    @Column(name = "date_invitation", nullable = false)
    private LocalDateTime dateInvitation = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUtilisateur")
    @JoinColumn(name = "id_utilisateur", foreignKey = @ForeignKey(name = "fk_invitation_utilisateur"))
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idSondage")
    @JoinColumn(name = "id_sondage", foreignKey = @ForeignKey(name = "fk_invitation_sondage"))
    private Sondage sondage;
}