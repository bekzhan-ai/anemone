package com.it75.anemone.service;

import com.it75.anemone.dto.UserDto;
import com.it75.anemone.entity.Role;
import com.it75.anemone.entity.User;
import com.it75.anemone.repository.RoleRepository;
import com.it75.anemone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    private void validateUserDto(UserDto userDto) throws ValidationException {
        if(isNull(userDto)){
            throw new ValidationException("dont know");
        }
        if(isNull(userDto.getUsername()) || userDto.getUsername().isEmpty()){
            throw new ValidationException("mdam");
        }
    }


    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public UserDto findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userConverter.fromUserToUserDto(userFromDb.orElse(new User()));
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userConverter::fromUserToUserDto)
                .collect(Collectors.toList());
    }

    public boolean saveUser(UserDto userDto) {
        User userFromDB = userRepository.findByUsername(userDto.getUsername());

        if (userFromDB != null) {
            return false;
        }

        userDto.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userDto.setPassword(bCryptPasswordEncoder.encode((userDto).getPassword()));
        userRepository.save(userConverter.fromUserDtoToUser(userDto));
        return true;
    }
    public List<UserDto> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", UserDto.class)
                .setParameter("paramId", idMin).getResultList();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
