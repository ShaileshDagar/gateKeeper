package com.GateKeeper.gateKeeper.controllers;

import com.GateKeeper.gateKeeper.domain.User;
import com.GateKeeper.gateKeeper.model.CommunityDTO;
import com.GateKeeper.gateKeeper.services.CommunityService;
import com.GateKeeper.gateKeeper.services.UtilityService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@AllArgsConstructor
public class CommunityManagerController {
    private static final Logger logger = LoggerFactory.getLogger(CommunityManagerController.class);
    private final CommunityService communityService;
    private final UtilityService utilityService;

    @RolesAllowed("COMMUNITY_MANAGER")
    @PostMapping(value = "/community/new", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommunityDTO> registerCommunity(@Valid @RequestBody CommunityDTO communityDTO,
                                                          BindingResult bindingResult, Authentication authentication){
        if(utilityService.errorsFound(bindingResult))
            return ResponseEntity.badRequest().body(communityDTO);
        if(communityService.findCommunityByCommunityNameAndAreaCode(communityDTO.getCommunityName(),
                communityDTO.getCommunityAddress().getAreaCode()).isPresent()){
            logger.error("Community already exists");
            return ResponseEntity.badRequest().body(communityDTO);
        }
        //adding the community manager who is creating this community into the set of community users.
        String communityManagerUsername = ((User) authentication.getPrincipal()).getUsername();
        communityDTO.getCommunityUsers().add(communityManagerUsername);

        communityService.createCommunity(communityDTO);

        // Adding community into community manager's set of communities.
        // I think this is redundant.
//        userService.addCommunity(communityManagerUsername, community);

        return new ResponseEntity<>(communityDTO, HttpStatus.CREATED);
    }

    @RolesAllowed("COMMUNITY_MANAGER")
    @DeleteMapping(value = "/community/delete", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCommunity(@Valid @RequestBody CommunityDTO communityDTO,
                                                  BindingResult bindingResult,
                                                  Authentication authentication) throws AccessDeniedException {
        if(utilityService.errorsFound(bindingResult))
            return ResponseEntity.badRequest().body("Request could not be Processed. Send valid Community Details");
        String communityManagerUsername = ((User) authentication.getPrincipal()).getUsername();
        communityService.deleteCommunity(communityDTO, communityManagerUsername);
        return ResponseEntity.ok("Community Deleted");
    }
}