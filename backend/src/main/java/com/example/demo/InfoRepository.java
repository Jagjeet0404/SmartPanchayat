package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InfoRepository extends JpaRepository<InfoItem, Long> {
    List<InfoItem> findByTypeOrderByIdDesc(String type);
}
