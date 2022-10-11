package com.korbiak.service.cloudinaryapi;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryManager {

    String uploadImage(MultipartFile image, String type, int id) throws IOException;
}
