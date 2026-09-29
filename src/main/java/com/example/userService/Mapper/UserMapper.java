package com.example.userService.Mapper;

import com.example.userService.DTO.UserDTO;
import com.example.userService.model.User;

public class UserMapper {

    public final User toEntity(UserDTO userDTO){
        User newUser = new User();

        newUser.setFirstName(userDTO.firstName());
        newUser.setLastName(userDTO.lastName());
        newUser.setEmail(userDTO.email());
        newUser.setBirth(userDTO.birth());
        newUser.setPostal(userDTO.postal());

        return newUser;
    }

    public final UserDTO toDto(User user){
        return new UserDTO(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getBirth(),
            user.getPostal()
        );
    }

}
