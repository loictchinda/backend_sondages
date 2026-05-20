package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateSondageRequest;
import com.example.demo.dto.SondageResponse;
import com.example.demo.dto.VoteRequest;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.SondageService;
import com.example.demo.service.VoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sondages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SondageController {

    private final SondageService sondageService;
    private final VoteService voteService;
    private final JwtUtils jwtUtils;
    
    @GetMapping
    public ResponseEntity<List<SondageResponse>> getAllSondages() {
        return ResponseEntity.ok(sondageService.getAllSondagesPublics());
    }

    @PostMapping
    public ResponseEntity<?> creerSondage(
            @Valid @RequestBody CreateSondageRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String pseudo = jwtUtils.getUserNameFromJwtToken(token);
            SondageResponse response = sondageService.creerSondage(request, pseudo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editerSondage(
            @PathVariable Long id,
            @Valid @RequestBody CreateSondageRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String pseudo = jwtUtils.getUserNameFromJwtToken(token);
            SondageResponse response = sondageService.editerSondage(id, request, pseudo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
 // ... ta méthode creerSondage

    @GetMapping("/{tokenPublic}")
    public ResponseEntity<?> consulterSondage(@PathVariable String tokenPublic) {
        try {
            SondageResponse response = sondageService.consulterParToken(tokenPublic);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // On renvoie une erreur 404 (Not Found) si le token n'existe pas
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerSondage(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extraction du pseudo à partir du token JWT
            String token = authHeader.replace("Bearer ", "");
            String pseudo = jwtUtils.getUserNameFromJwtToken(token);
            
            // Appel au service pour la suppression
            sondageService.supprimerSondage(id, pseudo);
            
            return ResponseEntity.ok("Sondage supprimé avec succès.");
        } catch (RuntimeException e) {
            // Renvoie une erreur 400 ou 403 selon le cas
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/votes")
    public ResponseEntity<?> voter(
            @PathVariable Long id,
            @Valid @RequestBody VoteRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String pseudo = jwtUtils.getUserNameFromJwtToken(token);
            
            voteService.enregistrerVote(id, request, pseudo);
            
            return ResponseEntity.ok("Vote enregistré avec succès !");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}/resultats")
    public ResponseEntity<?> getResultats(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(voteService.getResultats(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
