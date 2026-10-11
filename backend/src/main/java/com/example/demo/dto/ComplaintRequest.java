package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class ComplaintRequest {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;
    @Size(max = 2000)
    private String description;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
