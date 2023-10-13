package com.hrms.service;

import com.hrms.employeemanagement.exception.EmergencyContactNotFoundException;
import com.hrms.employeemanagement.exception.EmployeeNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

public interface UploadImageService {
    void uploadProjectImage(int id, MultipartFile file) throws IOException,
            EmergencyContactNotFoundException, EmployeeNotFoundException;
    ResponseEntity<byte[]> getProductImage(int id) throws IOException, EmployeeNotFoundException;
}