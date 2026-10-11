package com.example.demo;

import com.example.demo.dto.ComplaintRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintRepository repo;

    public ComplaintController(ComplaintRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Complaint create(@Valid @RequestBody ComplaintRequest req) {
        Complaint c = new Complaint();
        c.setUserEmail(req.getEmail().trim().toLowerCase());
        c.setTitle(req.getTitle().trim());
        c.setDescription(req.getDescription());
        return repo.save(c);
    }

    @GetMapping
    public List<Complaint> list(@RequestParam String email) {
        return repo.findByUserEmailOrderByIdDesc(email.trim().toLowerCase());
    }

    @GetMapping("/stats")
    public Map<String, Long> stats(@RequestParam String email) {
        String e = email.trim().toLowerCase();
        return Map.of(
                "total", repo.countByUserEmail(e),
                "pending", repo.countByUserEmailAndStatus(e, "PENDING"),
                "resolved", repo.countByUserEmailAndStatus(e, "RESOLVED"));
    }

    @PutMapping("/{id}/resolve")
    public Complaint resolve(@PathVariable Long id) {
        Complaint c = repo.findById(id).orElseThrow();
        c.setStatus("RESOLVED");
        return repo.save(c);
    }
}
