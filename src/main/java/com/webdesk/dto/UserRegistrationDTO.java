package com.webdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String bio;
    private byte[] profilePictureData;
    private String profilePictureType;
    private byte[] coverPictureData;
    private String coverPictureType;
}