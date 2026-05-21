package com.example.demo.repository;

import com.example.demo.entity.Sondage;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SondageRepository extends JpaRepository<Sondage, Long> {

    Optional<Sondage> findByTokenPublic(String tokenPublic);

    List<Sondage> findByVisibilite(Sondage.Visibilite visibilite);


    List<Sondage> findByCreateur_IdUtilisateur(Long idUtilisateur);
}