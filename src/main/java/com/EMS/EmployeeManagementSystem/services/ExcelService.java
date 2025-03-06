package com.EMS.EmployeeManagementSystem.services;

import com.EMS.EmployeeManagementSystem.entity.Role;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.repository.RoleRepository;
import com.EMS.EmployeeManagementSystem.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<String> savedUsersFromExcel(MultipartFile file) {
        List<String> duplicateEmails = new ArrayList<>();

   // public void savedUsersFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            rows.next();

            while (rows.hasNext()) {
                Row row = rows.next();

                String firstName = row.getCell(0).getStringCellValue();
                String lastName = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                String roleName = row.getCell(3).getStringCellValue();
                boolean status = row.getCell(4).getBooleanCellValue();
                String password = getCellValueAsString(row.getCell(5));

                if (userRepository.existsByEmail(email)) {
                    duplicateEmails.add(email);
                    continue; // Skip adding this user
                }

                Role role = roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

               // if(!userRepository.existsByEmail(email)) {
                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setRole(role);
                    user.setEmail(email);
                    user.setStatus(status);

                    userRepository.save(user);
                }

        } catch (IOException e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage());
        }

        return duplicateEmails;
    }
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
