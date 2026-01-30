package com.yrsd.medcheck.proxy.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.requests.UploadProfilePictureRequest;
import com.yrsd.medcheck.dtos.requests.UploadReportImage;
import com.yrsd.medcheck.dtos.responses.CloudServiceResponse;
import com.yrsd.medcheck.dtos.responses.UploadProfilePictureResponse;
import com.yrsd.medcheck.exceptions.FailedFileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryCloudService implements CloudService {

    private final Cloudinary cloudinary;

    @Override
    public CloudServiceResponse uploadProfilePicture(UploadProfilePictureRequest request) throws FailedFileUploadException {
        try {
            Map params = ObjectUtils.asMap(
                    "public_id", request.getUsername()+"_profile_picture",
                    "folder", "MedCheck/profile_pictures",
                    "display_name", request.getUsername()+"_profile_picture",
                    "overwrite", true,
                    "resource_type", "image"
            );
            Map<?,?> result = cloudinary.uploader().upload(request.getProfilePicture().getBytes(), params);
            String imageUrl = result.get("secure_url").toString();
            log.info("Profile Picture Uploaded for user '{}'", request.getUsername());
            return new CloudServiceResponse(imageUrl);
        } catch (IOException e){
            throw new FailedFileUploadException(e.getMessage());
        }
    }

    @Override
    public CloudServiceResponse uploadReportPicture(UploadReportImage request) throws FailedFileUploadException {
        return null;
    }
}
