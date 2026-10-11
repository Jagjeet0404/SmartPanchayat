package com.example.demo;

import com.example.demo.dto.InfoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** Notices, schemes and jobs: /api/info/{notices|schemes|jobs} */
@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final InfoRepository repo;

    public InfoController(InfoRepository repo) {
        this.repo = repo;
    }

    private static String toType(String path) {
        switch (path) {
            case "notices": return "NOTICE";
            case "schemes": return "SCHEME";
            case "jobs": return "JOB";
            default: return null;
        }
    }

    @GetMapping("/{kind}")
    public ResponseEntity<?> list(@PathVariable String kind) {
        String type = toType(kind);
        if (type == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Unknown category"));
        }
        List<InfoItem> items = repo.findByTypeOrderByIdDesc(type);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{kind}")
    public ResponseEntity<?> add(@PathVariable String kind, @Valid @RequestBody InfoRequest req) {
        String type = toType(kind);
        if (type == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Unknown category"));
        }
        InfoItem saved = repo.save(new InfoItem(type, req.getTitle().trim(), req.getDescription(), req.getDetails()));
        return ResponseEntity.ok(saved);
    }
}
