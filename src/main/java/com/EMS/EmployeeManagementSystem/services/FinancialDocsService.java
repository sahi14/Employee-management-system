package com.EMS.EmployeeManagementSystem.services;

import com.EMS.EmployeeManagementSystem.dto.FinancialDocsDto;
import com.EMS.EmployeeManagementSystem.entity.FinancialDocs;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.mapper.FinancialDocsMapper;
import com.EMS.EmployeeManagementSystem.repository.FinancialDocsRepository;
import com.EMS.EmployeeManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialDocsService {

    @Autowired
    private FinancialDocsRepository financialDocsRepository;

    @Autowired
    private UserRepository userRepository;
    public FinancialDocsDto addFinancialDocument(Long userId, FinancialDocsDto documentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        FinancialDocs document = FinancialDocsMapper.mapToFinancialDocs(documentDto, user);
        FinancialDocs savedDocument = financialDocsRepository.save(document);
        return FinancialDocsMapper.mapToFinancialDocsDto(savedDocument);
    }

    // Get all financial documents for a user
    public List<FinancialDocsDto> getFinancialDocuments(Long userId) {
        List<FinancialDocs> documents = financialDocsRepository.findByUserId(userId);
        return documents.stream()
                .map(FinancialDocsMapper::mapToFinancialDocsDto)
                .collect(Collectors.toList());
    }

    // Delete a financial document
    public void deleteFinancialDocument(Long documentId) {
        financialDocsRepository.deleteById(documentId);
    }
}
