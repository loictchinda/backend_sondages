package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InvitationId implements Serializable {
    @Column(name = "id_utilisateur")
    private Long idUtilisateur;

    @Column(name = "id_sondage")
    private Long idSondage;
}