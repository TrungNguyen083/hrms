package com.hrms.damservice;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface DamService {
    Map uploadFile(MultipartFile file) throws IOException;
    String getFileUrl(String publicId);
}
