package com.GateKeeper.gateKeeper.controllers;

import com.GateKeeper.gateKeeper.domain.User;
import com.GateKeeper.gateKeeper.model.Address;
import com.GateKeeper.gateKeeper.model.CommunityDTO;
import com.GateKeeper.gateKeeper.model.FlatDTO;
import com.GateKeeper.gateKeeper.model.UserDTO;
import com.GateKeeper.gateKeeper.services.CommunityService;
import com.GateKeeper.gateKeeper.services.UserService;
import com.GateKeeper.gateKeeper.services.UtilityService;
import com.GateKeeper.gateKeeper.services.VisitService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final CommunityService communityService;
    private final UtilityService utilityService;
    private final VisitService visitService;

    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping("/user/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserDTO(username));
    }

    @PatchMapping(value = "/user/address", consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<Address> updateUserAddress(@Valid @ModelAttribute Address address,
                                                     BindingResult bindingResult) {
        if(utilityService.errorsFound(bindingResult))
            return ResponseEntity.badRequest().body(address);
        String userName = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return ResponseEntity.ok(userService.updateUserAddress(userName, address));
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUser(Authentication authentication){
        String username = ((User) authentication.getPrincipal()).getUsername();
        //Deleting user from Associated Communities and its Flats
        communityService.removeUserFromCommunitiesAndFlats(username);
        //Finally, deleting the user
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("User Deleted");
    }

    @GetMapping("/community/find")
    public ResponseEntity<CommunityDTO> findCommunity(@Valid @RequestBody CommunityDTO communityDTO,
                                                      BindingResult bindingResult){
        if(utilityService.errorsFound(bindingResult))
            return ResponseEntity.badRequest().body(communityDTO);
        Optional<CommunityDTO> optionalCommunityDTO
                = communityService.findCommunityByCommunityNameAndAreaCode(communityDTO.getCommunityName(),
                                                                            communityDTO.getCommunityAddress()
                                                                                        .getAreaCode());
        return optionalCommunityDTO.map(ResponseEntity::ok)
                                    .orElseGet(() -> ResponseEntity.badRequest().body(communityDTO));
    }

    @RolesAllowed({"USER", "COMMUNITY_MANAGER"})
    @PatchMapping("/community/join/flat/{flatNumber}")
    public ResponseEntity<String> joinCommunity(@Valid @RequestBody CommunityDTO communityDTO,
                                                @PathVariable String flatNumber,
                                                BindingResult bindingResult,
                                                Authentication authentication){
        if(utilityService.errorsFound(bindingResult) || flatNumber==null || flatNumber.isBlank())
            return ResponseEntity.badRequest().body("Please send valid details of the Community");
        String username = ((User) authentication.getPrincipal()).getUsername();
        String response = communityService.requestToJoinCommunity(communityDTO, username, flatNumber);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/visit")
    public ResponseEntity<String> createVisit(@Valid @RequestBody FlatDTO flatDTO, BindingResult bindingResult,
                                              Authentication authentication){
        if(utilityService.errorsFound(bindingResult))
            return ResponseEntity.badRequest().body("Please send valid details of the Flat");
        String username = ((User) authentication.getPrincipal()).getUsername();
        //check if the user is already part of the community or not.
        String response = "User is a part of the community, you don't need to make a visit.";
        if(!communityService.checkUserPartOfCommunity(flatDTO.getCommunityDTO(), username))
            response = visitService.createVisit(flatDTO,username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/outbound/visit")
    public ResponseEntity<String> completeVisit(@Valid @RequestBody CommunityDTO communityDTO, BindingResult bindingResult,
                                                Authentication authentication){
        String username = ((User) authentication.getPrincipal()).getUsername();
        //check if the user is already part of the community or not.
        String response = "User is a part of the community, you don't need to mark an outbound visit.";
        if(!communityService.checkUserPartOfCommunity(communityDTO, username))
            response = visitService.completeVisit(communityDTO, username);
        return ResponseEntity.ok(response);
    }
}


//    @PatchMapping(value = "/user/address", consumes = {MediaType.ALL_VALUE})
//    public ResponseEntity<Address> updateUserAddress(@RequestParam Map<String,String> map){
//        Address address = new Address();
//        if(map.size()!=4)
//        {
//            return ResponseEntity.badRequest().body(address);
//        }
//        address.setAreaCode(map.get("areaCode"));
//        address.setCity(map.get("city"));
//        address.setState(map.get("state"));
//        address.setCountry(map.get("country"));
//        String userName = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//        return ResponseEntity.ok(userService.updateUserAddress(userName, address));
//    }