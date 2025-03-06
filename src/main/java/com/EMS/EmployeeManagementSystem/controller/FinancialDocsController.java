package com.EMS.EmployeeManagementSystem.controller;

import com.EMS.EmployeeManagementSystem.dto.FinancialDocsDto;
import com.EMS.EmployeeManagementSystem.entity.FinancialDocs;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.repository.FinancialDocsRepository;
import com.EMS.EmployeeManagementSystem.repository.UserRepository;
import com.EMS.EmployeeManagementSystem.services.FinancialDocsService;
import com.EMS.EmployeeManagementSystem.services.S3Service;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/financial-docs")
public class FinancialDocsController {

    @Autowired
    private FinancialDocsService financialDocsService;

    @Autowired private S3Service s3Service;

    @Autowired private UserRepository userRepository;

    @Autowired private FinancialDocsRepository financialDocsRepository;


    // Add a financial document for a user
    @PostMapping("/add")
    public FinancialDocsDto addFinancialDocument(@RequestParam Long userId, @RequestBody FinancialDocsDto documentDto) {
        return financialDocsService.addFinancialDocument(userId, documentDto);
    }

    // Get financial documents for a user
    @PostMapping("/get")
    public List<FinancialDocsDto> getFinancialDocuments(@RequestParam Long userId) {
        return financialDocsService.getFinancialDocuments(userId);
    }

    // Delete a financial document
    @PostMapping("/delete")
    public String deleteFinancialDocument(@RequestParam Long documentId) {
        financialDocsService.deleteFinancialDocument(documentId);
        return "Document deleted successfully.";
    }

    // API to upload financial document to S3
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("userId") Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // Upload file to S3 and get the URL
            String documentUrl = s3Service.uploadFile(file);
            System.out.println("url {}"+ documentUrl);
            System.out.println("name " + file.getOriginalFilename());
            // Create and save document metadata
            FinancialDocs financialDoc = new FinancialDocs();
            financialDoc.setUser(user);
            financialDoc.setDocumentName(file.getOriginalFilename());
          //  financialDoc.setDocumentUrl("abc");
            financialDoc.setUploadedAt(LocalDateTime.now());

            financialDocsRepository.save(financialDoc);

            return ResponseEntity.ok("File uploaded successfully and metadata saved.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }


    // API to download financial document from S3
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) throws IOException {
        S3Object s3Object = s3Service.downloadFile(fileName);
        InputStream inputStream = s3Object.getObjectContent();

        byte[] bytes = inputStream.readAllBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(bytes);
    }
}
