package com.korbiak.service.cloudinaryapi.impl;

import com.cloudinary.Cloudinary;
import com.google.common.collect.ImmutableMap;
import com.korbiak.service.cloudinaryapi.CloudinaryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryManagerImpl implements CloudinaryManager {

    private final Cloudinary cloudinary;

    private static final String PUBLIC_ID = "public_id";
    private static final String OVERWRITE = "overwrite";
    private static final String RESOURCE_TYPE = "resource-type";

    private static final String RES_TYPE = "image";
    private static final Boolean IS_OVERWRITE = true;
    private static final String FOLDER = "elec-project";

    @Override
    @SuppressWarnings("unchecked")
    public String uploadImage(MultipartFile image, String type, int id) throws IOException {

        Map<String, Object> params = ImmutableMap.of(
                PUBLIC_ID, FOLDER + "/" + type + "/" + id + "-id",
                OVERWRITE, IS_OVERWRITE,
                RESOURCE_TYPE, RES_TYPE
        );

        Map<Object, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), params);

        return (String) uploadResult.get("url");
    }
}
