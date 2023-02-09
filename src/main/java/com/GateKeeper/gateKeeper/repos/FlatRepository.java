package com.GateKeeper.gateKeeper.repos;

import com.GateKeeper.gateKeeper.domain.Community;
import com.GateKeeper.gateKeeper.domain.Flat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    Optional<Flat> findByFlatNumberIgnoreCaseAndFlatCommunity(String flatNumber, Community community);
}
