package com.GateKeeper.gateKeeper.services;

import com.GateKeeper.gateKeeper.domain.Community;
import com.GateKeeper.gateKeeper.domain.Flat;
import com.GateKeeper.gateKeeper.domain.User;
import com.GateKeeper.gateKeeper.domain.Visit;
import com.GateKeeper.gateKeeper.model.CommunityDTO;
import com.GateKeeper.gateKeeper.model.FlatDTO;
import com.GateKeeper.gateKeeper.repos.VisitRepository;
import com.GateKeeper.gateKeeper.util.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Transactional
@Service
@AllArgsConstructor
public class VisitService {
    private static final Logger logger = LoggerFactory.getLogger(VisitService.class);

    private final VisitRepository visitRepository;
    private final CommunityService communityService;
    private final FlatService flatservice;
    private final UserService userService;
    private final RedisTemplate<String, String> redisVisitTemplate;

    public String createVisit(FlatDTO flatDTO, String username) {
        User visitingUser = (User) userService.loadUserByUsername(username);
        Visit visit = new Visit();
        CommunityDTO communityDTO = flatDTO.getCommunityDTO();
        Community community = communityService.findCommunity(communityDTO.getCommunityName(),
                                                                communityDTO.getCommunityAddress().getAreaCode());
        Flat visitFlat = flatservice.findFlat(flatDTO.getFlatNumber(), community)
                        .orElseThrow(() -> new NotFoundException("The flat you are trying to visit does not exist"));
        visit.setVisitFlat(visitFlat);
        visit.setVisitingUser(visitingUser);
        visit.setVisitInBoundTime(LocalDateTime.now());
        visit = visitRepository.save(visit);
        redisVisitTemplate.opsForValue().set(username, visit.getVisitID().toString());
        return "Visit has been successfully created.";
    }

    public String completeVisit(CommunityDTO communityDTO, String username) {
        String visitIDValue = redisVisitTemplate.opsForValue().get(username);
        if(visitIDValue==null)
            return "There is no ongoing visit in this community for the given user";
        Visit visit = visitRepository.findById(Long.parseLong(visitIDValue))
                                        .orElseThrow(() -> new NotFoundException("Visit not found in the database"));
        redisVisitTemplate.delete(username);
        if(visit.getVisitOutBoundTime()!=null)
            return "Visit was already Completed";
        visit.setVisitOutBoundTime(LocalDateTime.now());
        visitRepository.save(visit);
        return "Visit has been Completed";
    }
}
