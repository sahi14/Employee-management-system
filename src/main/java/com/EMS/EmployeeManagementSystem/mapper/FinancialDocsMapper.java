package com.EMS.EmployeeManagementSystem.mapper;

import com.EMS.EmployeeManagementSystem.dto.FinancialDocsDto;
import com.EMS.EmployeeManagementSystem.entity.FinancialDocs;
import com.EMS.EmployeeManagementSystem.entity.User;
public class FinancialDocsMapper {
    public static FinancialDocsDto mapToFinancialDocsDto(FinancialDocs financialDocs) {
        return new FinancialDocsDto(
                financialDocs.getId(),
                financialDocs.getUser().getId(),
                financialDocs.getDocumentName(),
              //  financialDocs.getDocumentUrl(),
                financialDocs.getUploadedAt()
        );
    }
    public static FinancialDocs mapToFinancialDocs(FinancialDocsDto financialDocsDto, User user) {
        return new FinancialDocs(
                financialDocsDto.getId(),
                user,
                financialDocsDto.getDocumentName(),
               // financialDocsDto.getDocumentUrl(),
                financialDocsDto.getUploadedAt()
        );
    }
}
