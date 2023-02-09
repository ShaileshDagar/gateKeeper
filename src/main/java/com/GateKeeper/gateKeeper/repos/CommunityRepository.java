package com.GateKeeper.gateKeeper.repos;

import com.GateKeeper.gateKeeper.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByCommunityNameIgnoreCaseAndCommunityAddress_AreaCodeIgnoreCase(String communityName,
                                                                                            String areaCode);
}
