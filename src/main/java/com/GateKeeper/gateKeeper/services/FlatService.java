package com.GateKeeper.gateKeeper.services;

import com.GateKeeper.gateKeeper.domain.Community;
import com.GateKeeper.gateKeeper.domain.Flat;
import com.GateKeeper.gateKeeper.repos.FlatRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class FlatService {

    private static final Logger logger = LoggerFactory.getLogger(FlatService.class);
    private final FlatRepository flatRepository;

    public Flat createFlat(Community community, String flatNumber){
        Optional<Flat> optionalFlat = findFlat(flatNumber, community);
        if(optionalFlat.isPresent())
            return optionalFlat.get();
        Flat flat = new Flat();
        flat.setFlatCommunity(community);
        flat.setFlatNumber(flatNumber);
        return flatRepository.save(flat);
    }

    public Optional<Flat> findFlat(String flatNumber, Community community){
        return flatRepository.findByFlatNumberIgnoreCaseAndFlatCommunity(flatNumber, community);
    }
}
