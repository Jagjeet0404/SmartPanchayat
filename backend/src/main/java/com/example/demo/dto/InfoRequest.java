package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class InfoRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title too long")
    private String title;
    @Size(max = 2000, message = "Description too long")
    private String description;
    @Size(max = 250, message = "Details too long")
    private String details;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
