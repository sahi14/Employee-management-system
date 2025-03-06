package com.EMS.EmployeeManagementSystem.controller;

import com.EMS.EmployeeManagementSystem.services.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")

public class NotificationsController {

    @Autowired
    private NotificationsService notificationsService;
    @Value("${spring.mail.from}")
    private String fromEmail;

    @PostMapping("/test")
    public String sendTestEmail(@RequestParam String toEmail) {
        notificationsService.sendEmail(toEmail, "Test Email", "This is a test email from EMS.");
        return "Test email sent to " + toEmail;
    }

    //Notify Admin about pending approvals
    @PostMapping("/reminder/admin")
    public String sendAdminReminder() {
        notificationsService.sendEmail(fromEmail, "User Approval Pending",
                "Reminder: Approval is pending for user registration.");
        return "Reminder email sent to Admin.";
    }

    //Notify User when their registration is approved
    @PostMapping("/approval")
    public String sendUserApprovalEmail(@RequestParam String userEmail) {
        notificationsService.sendEmail(userEmail, "Registration Approved",
                "Your account has been approved! You can now log in.");
        return "Approval email sent to " + userEmail;
    }
}
