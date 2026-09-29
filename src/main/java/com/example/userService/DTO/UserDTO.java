package com.example.userService.DTO;

import java.util.Date;

import com.example.userService.model.Postal;

public record UserDTO(
    String firstName,
    String lastName,
    String email,
    Date birth,
    Postal postal
) {
    
}
