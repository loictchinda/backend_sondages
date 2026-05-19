package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "option_reponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionReponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_option")
    private Long idOption;

    @Column(name = "texte_option", nullable = false)
    private String texteOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sondage", nullable = false, foreignKey = @ForeignKey(name = "fk_option_sondage"))
    private Sondage sondage;
}