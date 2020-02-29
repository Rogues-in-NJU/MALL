package edu.nju.mall.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {

    Map<String, String> saveImage(MultipartFile file);

    void deleteImage(String address);

}