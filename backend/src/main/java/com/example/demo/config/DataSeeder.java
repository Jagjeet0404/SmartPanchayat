package com.example.demo.config;

import com.example.demo.InfoItem;
import com.example.demo.InfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Inserts sample notices, schemes and jobs the first time the database is empty. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final InfoRepository repo;

    public DataSeeder(InfoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;
        repo.saveAll(List.of(
            new InfoItem("NOTICE", "Gram Sabha Meeting", "Monthly Gram Sabha meeting at the Panchayat Bhawan. All villagers are invited.", "Sample notice"),
            new InfoItem("NOTICE", "Water Supply Maintenance", "Water supply will be affected during pipeline maintenance work.", "Sample notice"),
            new InfoItem("NOTICE", "Vaccination Camp", "Free health check-up and vaccination camp at the village health centre.", "Sample notice"),
            new InfoItem("SCHEME", "PM-KISAN", "Income support of Rs 6,000 per year to eligible farmer families, paid in three instalments.", "Apply: nearest CSC / pmkisan.gov.in"),
            new InfoItem("SCHEME", "MGNREGA", "Guarantees up to 100 days of wage employment per year to rural households that ask for unskilled work.", "Apply: Gram Panchayat office"),
            new InfoItem("SCHEME", "Ayushman Bharat (PM-JAY)", "Health cover of up to Rs 5 lakh per family per year for eligible families.", "Apply: empanelled hospital / CSC"),
            new InfoItem("SCHEME", "PM Awas Yojana (Gramin)", "Financial assistance for building a pucca house for eligible rural families.", "Apply: Gram Panchayat office"),
            new InfoItem("JOB", "Village Data Entry Operator", "Basic computer knowledge needed. Work at the Panchayat office.", "Sample job | Contact: Panchayat office"),
            new InfoItem("JOB", "Anganwadi Helper", "Help with child care and nutrition programmes in the village.", "Sample job | Contact: Block office"),
            new InfoItem("JOB", "MGNREGA Worker", "Wage work on local road, pond and farm projects.", "Sample job | Register: Gram Panchayat")
        ));
    }
}
