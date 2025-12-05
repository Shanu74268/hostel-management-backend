package com.springsecurity.springsecurity.service.s3service;

import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3ProfilePicService {

    private final StudentRepository studentRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public S3ProfilePicService(StudentRepository studentRepository, S3Client s3Client) {
        this.studentRepository = studentRepository;
        this.s3Client = s3Client;
    }

    // ✅ Upload file and save URL in DB (debug version)
    public String uploadProfilePic(MultipartFile file, Long studentId) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        String fileName = "profile_" + studentId + "_" + UUID.randomUUID() + ".jpg";

        try {
            // Upload to S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            // Construct URL
            String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

            // Debug log
            System.out.println("✅ Uploaded Profile Pic URL: " + fileUrl);

            // Save URL in DB
            student.setProfilePicUrl(fileUrl);
            studentRepository.save(student);

            return fileUrl;

        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload to S3: " + e.awsErrorDetails().errorMessage());
        }
    }

    // ✅ Optional download (unchanged)
    public byte[] downloadProfilePic(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        String fileUrl = student.getProfilePicUrl();
        if (fileUrl == null) {
            throw new RuntimeException("Profile picture not uploaded yet for this student");
        }

        String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
    }
}
