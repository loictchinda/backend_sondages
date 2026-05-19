package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sondage", uniqueConstraints = {
    @UniqueConstraint(name = "uk_token_public", columnNames = "token_public")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sondage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sondage")
    private Long idSondage;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @Column(name = "token_public", nullable = false, length = 36)
    private String tokenPublic = UUID.randomUUID().toString();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibilite visibilite = Visibilite.public_enum; // Evite conflit mot clé SQL 'public'

    @Builder.Default
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false, foreignKey = @ForeignKey(name = "fk_sondage_utilisateur"))
    private Utilisateur createur;

    @OneToMany(mappedBy = "sondage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionReponse> options;

    public enum Visibilite {
        @jakarta.persistence.MapKeyColumn(name = "public") public_enum,
        prive
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
    }
}