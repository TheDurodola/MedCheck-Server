package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UploadProfilePictureRequest {
    private MultipartFile profilePicture;
    private String username;
}
