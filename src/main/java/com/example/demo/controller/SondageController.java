package com.example.demo.controller;

import com.example.demo.dto.CreateSondageRequest;
import com.example.demo.dto.SondageResponse;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.SondageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sondages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SondageController {

    private final SondageService sondageService;
    private final JwtUtils jwtUtils;

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
}