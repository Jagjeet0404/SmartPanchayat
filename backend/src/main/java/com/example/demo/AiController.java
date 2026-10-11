package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/** AI helper: answers villagers' questions about schemes and jobs using the Claude API. */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final InfoRepository info;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

    @Value("${ai.api-key:}")
    private String apiKey;

    @Value("${ai.model:claude-sonnet-5-5}")
    private String model;

    public AiController(InfoRepository info) {
        this.info = info;
    }

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        if (question == null || question.isBlank()) {
            return err(HttpStatus.BAD_REQUEST, "Question is required");
        }
        if (question.length() > 1000) {
            return err(HttpStatus.BAD_REQUEST, "Question too long (max 1000 characters)");
        }
        if (apiKey == null || apiKey.isBlank()) {
            return err(HttpStatus.SERVICE_UNAVAILABLE,
                    "AI is not configured. Put your Anthropic API key in apikey.txt and restart.");
        }
        try {
            StringBuilder ctx = new StringBuilder();
            ctx.append("Schemes in this village portal:\n");
            for (InfoItem i : info.findByTypeOrderByIdDesc("SCHEME")) {
                ctx.append("- ").append(i.getTitle()).append(": ").append(i.getDescription())
                   .append(" (").append(i.getDetails()).append(")\n");
            }
            ctx.append("\nJobs in this village portal:\n");
            for (InfoItem i : info.findByTypeOrderByIdDesc("JOB")) {
                ctx.append("- ").append(i.getTitle()).append(": ").append(i.getDescription())
                   .append(" (").append(i.getDetails()).append(")\n");
            }
            String system = "You are a helpful assistant for an Indian village Panchayat portal. "
                    + "Answer in simple language, in the same language the user writes (Hindi, Hinglish or English). "
                    + "Use the portal data below when relevant and suggest suitable schemes or jobs. "
                    + "If unsure about eligibility or details, tell the user to confirm at the Panchayat office. "
                    + "Keep answers short.\n\n" + ctx;

            ObjectNode req = mapper.createObjectNode();
            req.put("model", model);
            req.put("max_tokens", 800);
            req.put("system", system);
            ArrayNode msgs = req.putArray("messages");
            ObjectNode m = msgs.addObject();
            m.put("role", "user");
            m.put("content", question);

            HttpRequest r = HttpRequest.newBuilder(URI.create("https://api.anthropic.com/v1/messages"))
                    .timeout(Duration.ofSeconds(60))
                    .header("content-type", "application/json")
                    .header("x-api-key", apiKey.trim())
                    .header("anthropic-version", "2023-06-01")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(req)))
                    .build();
            HttpResponse<String> res = http.send(r, HttpResponse.BodyHandlers.ofString());
            JsonNode json = mapper.readTree(res.body());
            if (res.statusCode() != 200) {
                String msg = json.path("error").path("message").asText("AI request failed");
                return err(HttpStatus.BAD_GATEWAY, "AI error: " + msg);
            }
            StringBuilder text = new StringBuilder();
            for (JsonNode block : json.path("content")) {
                if ("text".equals(block.path("type").asText())) text.append(block.path("text").asText());
            }
            return ResponseEntity.ok(Map.of("answer", text.toString()));
        } catch (Exception e) {
            return err(HttpStatus.BAD_GATEWAY, "Could not reach the AI service");
        }
    }

    private static ResponseEntity<Map<String, String>> err(HttpStatus s, String msg) {
        return ResponseEntity.status(s).body(Map.of("error", msg));
    }
}
