package com.EMS.EmployeeManagementSystem.services;

        import com.amazonaws.services.s3.AmazonS3;
        import com.amazonaws.services.s3.model.ObjectMetadata;
        import com.amazonaws.services.s3.model.PutObjectRequest;
        import com.amazonaws.services.s3.model.S3Object;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Service;
        import org.springframework.web.multipart.MultipartFile;

        import java.io.IOException;
        import java.io.InputStream;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // Upload file to S3 bucket
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
            return "File uploaded successfully: " + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file";
        }
    }

    // Download file from S3 bucket
    public S3Object downloadFile(String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }
}
