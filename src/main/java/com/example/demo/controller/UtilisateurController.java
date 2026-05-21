package com.example.demo.controller;

import com.example.demo.dto.HistoriqueVoteResponse;
import com.example.demo.dto.UtilisateurResponse;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.UtilisateurService;
import com.example.demo.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UtilisateurController {

    private final VoteService voteService;
    private final UtilisateurService utilisateurService;
    private final JwtUtils jwtUtils;
    
    @GetMapping
    public ResponseEntity<List<UtilisateurResponse>> getAllUsers() {
        try {
            
            return ResponseEntity.ok(utilisateurService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/me/votes")
    public ResponseEntity<?> getHistoriqueVotes(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extraction du pseudo depuis le token
            String token = authHeader.replace("Bearer ", "");
            String pseudo = jwtUtils.getUserNameFromJwtToken(token);

            // Récupération de l'historique
            List<HistoriqueVoteResponse> historique = voteService.getHistoriqueVotes(pseudo);
            
            return ResponseEntity.ok(historique);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}