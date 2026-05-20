package com.example.demo.repository;

import com.example.demo.entity.Sondage;
import com.example.demo.entity.Utilisateur;
import com.example.demo.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    long countByOptionReponseIdOption(Long idOption);
    long countBySondageIdSondage(Long idSondage);
    boolean existsByUtilisateurAndSondage(Utilisateur utilisateur, Sondage sondage);
    
    // NOUVELLE MÉTHODE
    List<Vote> findByUtilisateurOrderByDateVoteDesc(Utilisateur utilisateur);
}