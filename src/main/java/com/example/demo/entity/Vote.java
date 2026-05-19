package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote", uniqueConstraints = {
    @UniqueConstraint(name = "uk_utilisateur_sondage", columnNames = {"id_utilisateur", "id_sondage"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vote")
    private Long idVote;

    @Builder.Default
    @Column(name = "date_vote", nullable = false, updatable = false)
    private LocalDateTime dateVote = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false, foreignKey = @ForeignKey(name = "fk_vote_utilisateur"))
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_option", nullable = false, foreignKey = @ForeignKey(name = "fk_vote_option"))
    private OptionReponse optionReponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sondage", nullable = false, foreignKey = @ForeignKey(name = "fk_vote_sondage"))
    private Sondage sondage;
}