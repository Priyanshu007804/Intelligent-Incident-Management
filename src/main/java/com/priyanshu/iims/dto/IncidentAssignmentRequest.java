package com.priyanshu.iims.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class IncidentAssignmentRequest {

    @NotBlank(message = "Engineer email is required")
    @Email(message = "Invalid engineer email format")
    private String engineerEmail;

    public IncidentAssignmentRequest() {
    }

    public String getEngineerEmail() {
        return engineerEmail;
    }

    public void setEngineerEmail(String engineerEmail) {
        this.engineerEmail = engineerEmail;
    }
}