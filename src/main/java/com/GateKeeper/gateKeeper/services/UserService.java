package com.GateKeeper.gateKeeper.services;

import com.GateKeeper.gateKeeper.domain.Community;
import com.GateKeeper.gateKeeper.domain.Flat;
import com.GateKeeper.gateKeeper.domain.User;
import com.GateKeeper.gateKeeper.model.Address;
import com.GateKeeper.gateKeeper.model.UserDTO;
import com.GateKeeper.gateKeeper.repos.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Transactional
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO getUserDTO(String username){
        User user = (User) loadUserByUsername(username);
        return mapUserToDTO(user);
    }

    private UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getUserRole());
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public boolean userNameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public String createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserRole(userDTO.getRole());
        userRepository.save(user);
        userDTO.setPassword(user.getPassword());
        return "User Created";
    }

    public Address updateUserAddress(String username, Address userAddress) {
        User user = (User) loadUserByUsername(username);
        user.setUserAddress(userAddress);
        userRepository.save(user);
        return userAddress;
    }

    public void addCommunity(String username, Community community){
        User communityUser = (User)loadUserByUsername(username);
        communityUser.getUserCommunities().add(community);
        userRepository.save(communityUser);
    }

    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void removeAllUsers(Set<User> communityUsers, Flat flat) {
        communityUsers.forEach(communityUser -> communityUser.getUserFlats().remove(flat));
    }

    public void removeUserFromFlats(User user) {
        Set<Flat> userFlats = user.getUserFlats();
        userFlats.forEach(flat -> flat.getFlatUsers().remove(user));
    }
}
