package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserEmailOrderByIdDesc(String userEmail);
    long countByUserEmail(String userEmail);
    long countByUserEmailAndStatus(String userEmail, String status);
}
