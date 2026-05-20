package com.example.demo.repository;

import com.example.demo.entity.Invitation;
import com.example.demo.entity.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, InvitationId> {
}