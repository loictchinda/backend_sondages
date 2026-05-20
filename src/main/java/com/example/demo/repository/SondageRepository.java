package com.example.demo.repository;

import com.example.demo.entity.Sondage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SondageRepository extends JpaRepository<Sondage, Long> {

    Optional<Sondage> findByTokenPublic(String tokenPublic);
}