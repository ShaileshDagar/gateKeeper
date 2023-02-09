package com.GateKeeper.gateKeeper.services;

import com.GateKeeper.gateKeeper.domain.Community;
import com.GateKeeper.gateKeeper.domain.Flat;
import com.GateKeeper.gateKeeper.domain.User;
import com.GateKeeper.gateKeeper.model.CommunityDTO;
import com.GateKeeper.gateKeeper.model.Role;
import com.GateKeeper.gateKeeper.repos.CommunityRepository;
import com.GateKeeper.gateKeeper.util.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class CommunityService {
    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);
    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final FlatService flatService;

    public void createCommunity(final CommunityDTO communityDTO){
        Community community = mapDTOToEntity(communityDTO);
        communityRepository.save(community);
    }

    private Community mapDTOToEntity(final CommunityDTO communityDTO) {
        Community community = new Community();
        community.setCommunityName(communityDTO.getCommunityName());
        community.setCommunityAddress(communityDTO.getCommunityAddress());
        Set<String> communityUserUsernames = communityDTO.getCommunityUsers();
        community.setCommunityUsers(communityUserUsernames == null ? null : communityUserUsernames.stream()
                .map(username -> (User)userService.loadUserByUsername(username))
                .collect(Collectors.toSet())
        );
        return community;
    }

    private CommunityDTO mapEntityToDTO(final Community community){
        CommunityDTO communityDTO = new CommunityDTO();
        communityDTO.setCommunityName(community.getCommunityName());
        communityDTO.setCommunityAddress(community.getCommunityAddress());
        Set<User> communityUsers = community.getCommunityUsers();
        communityDTO.setCommunityUsers(communityUsers==null ? null : communityUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toSet())
        );
        return communityDTO;
    }
    public Optional<CommunityDTO> findCommunityByCommunityNameAndAreaCode(final String communityName,
                                                                          final String areaCode) {
        Optional<Community> optionalCommunity
                    = communityRepository
                        .findByCommunityNameIgnoreCaseAndCommunityAddress_AreaCodeIgnoreCase(communityName,areaCode);
        return optionalCommunity.map(this::mapEntityToDTO).or(() -> Optional.ofNullable(null));
    }

    public void removeUserFromCommunitiesAndFlats(String username){
        User user = (User) userService.loadUserByUsername(username);
        Set<Community> communities = user.getUserCommunities();
        for(Community community : communities){
            community = communityRepository
                    .findById(community.getCommunityID())
                    .orElseThrow(() -> new NotFoundException("Community not found while removing the user from it"));
            community.getCommunityUsers().remove(user);
            communityRepository.save(community);
        }
        //Delete User from all its flats
        userService.removeUserFromFlats(user);
    }

    public void deleteCommunity(final CommunityDTO communityDTO,
                                final String communityManagerUsername) throws AccessDeniedException{
        //Should we also delete the community from the set of all users associated with it?
        // I don't think that we need to as there is nothing to be done in the database.
        // But, for object consistency I have deleted the community from set of community manager's communities.
        User communityManager = (User) userService.loadUserByUsername(communityManagerUsername);
        Optional<Community> optionalCommunity
                = communityRepository
                .findByCommunityNameIgnoreCaseAndCommunityAddress_AreaCodeIgnoreCase(communityDTO.getCommunityName(),
                                                                    communityDTO.getCommunityAddress().getAreaCode());
        Community community = optionalCommunity
                    .orElseThrow(() -> new NotFoundException("The Community you are trying to delete does not exist"));

        Set<User> communityUsers = community.getCommunityUsers();
        if(communityUsers.contains(communityManager)){
//            communityUsers.forEach(user -> user.getUserCommunities().remove(community));
            communityManager.getUserCommunities().remove(community);
//            userRepository.save(communityManager);

            //Deleting all the user-flat relationships in this community.
            Set<Flat> communityFlats = community.getCommunityFlats();
            communityFlats.forEach(flat -> userService.removeAllUsers(communityUsers, flat));

            //deleting all the user-community relationship.
            // This might happen automatically as Community is owner of the relationship.

            communityRepository.deleteById(community.getCommunityID());
        }
        else
            throw new AccessDeniedException("You don't have authorization to delete this Community "
                                                                                    + "as you are not a part of it");
    }

    public String requestToJoinCommunity(final CommunityDTO communityDTO, String username, String flatNumber) {
        User user = (User) userService.loadUserByUsername(username);
        Community community = findCommunity(communityDTO.getCommunityName(),
                                                communityDTO.getCommunityAddress().getAreaCode());
        Set<User> communityUsers = community.getCommunityUsers();
        if(communityUsers.contains(user))
            return "User is already a part of the community";
        if(!user.getUserRole().equals(Role.COMMUNITY_MANAGER)){
            Flat userFlat = flatService.createFlat(community, flatNumber);
            user.getUserFlats().add(userFlat);
        }
        communityUsers.add(user);
        user.getUserCommunities().add(community);
        userService.saveUser(user);
        communityRepository.save(community);
        return "Your Request has been successfully recorded";
    }

    public Community findCommunity(String communityName, String communityAddressAreaCode){
        return communityRepository
                .findByCommunityNameIgnoreCaseAndCommunityAddress_AreaCodeIgnoreCase(communityName,
                        communityAddressAreaCode)
                .orElseThrow(() -> new NotFoundException("The Community you are trying to join does not exist"));
    }

    public boolean checkUserPartOfCommunity(CommunityDTO communityDTO, String username) {
        User user = (User) userService.loadUserByUsername(username);
        Community community = findCommunity(communityDTO.getCommunityName(),
                communityDTO.getCommunityAddress().getAreaCode());
        Set<User> communityUsers = community.getCommunityUsers();
        return communityUsers.contains(user);
    }
}