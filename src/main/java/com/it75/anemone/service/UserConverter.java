package com.it75.anemone.service;

import com.it75.anemone.dto.UserDto;
import com.it75.anemone.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
        public UserDto fromUserToUserDto(User user) {
            return UserDto.builder()
                    .id(user.getId())
                    .password(user.getPassword())
                    .passwordConfirm(user.getPasswordConfirm())
                    .roles(user.getRoles())
                    .build();
        }
        public User fromUserDtoToUser(UserDto userDto) {
            User user = new User();
            user.setId(userDto.getId());
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setPasswordConfirm(userDto.getPasswordConfirm());
            user.setRoles(userDto.getRoles());
            return user;
        }


    }
