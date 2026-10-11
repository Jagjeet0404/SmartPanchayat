package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "info_items")
public class InfoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** NOTICE, SCHEME or JOB */
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    /** Extra line: date / benefit / location+salary etc. */
    private String details;

    private LocalDateTime createdAt = LocalDateTime.now();

    public InfoItem() {}

    public InfoItem(String type, String title, String description, String details) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.details = details;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
