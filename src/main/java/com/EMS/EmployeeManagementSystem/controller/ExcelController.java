package com.EMS.EmployeeManagementSystem.controller;

import com.EMS.EmployeeManagementSystem.services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("message", "Please upload a valid Excel file.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            List<String> duplicateEmails = excelService.savedUsersFromExcel(file);
            response.put("message", "File uploaded and processed successfully.");
            response.put("duplicates", duplicateEmails);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error processing file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid Excel file.");
//        }
//        try {
//            excelService.savedUsersFromExcel(file);
//            return ResponseEntity.ok("File uploaded and processed successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error processing file: " + e.getMessage());
//        }
//    }
}
