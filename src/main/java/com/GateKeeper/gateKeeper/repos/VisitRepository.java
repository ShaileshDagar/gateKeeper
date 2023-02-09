package com.GateKeeper.gateKeeper.repos;

import com.GateKeeper.gateKeeper.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    Optional<Visit> findByVisitingUser_Username(String username);
}
