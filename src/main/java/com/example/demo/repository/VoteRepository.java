package com.example.demo.repository;

import com.example.demo.entity.Sondage;
import com.example.demo.entity.Utilisateur;
import com.example.demo.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUtilisateurAndSondage(Utilisateur utilisateur, Sondage sondage);
}