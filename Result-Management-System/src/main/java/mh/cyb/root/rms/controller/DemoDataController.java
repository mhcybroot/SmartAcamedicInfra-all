package mh.cyb.root.rms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mh.cyb.root.rms.service.DemoDataService;

import java.util.Map;

/**
 * Public REST endpoint to trigger demo data generation for exams, marks, and attendance.
 * Accessible from the Vue portal sidebar without authentication.
 */
@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoDataController {

    @Autowired
    private DemoDataService demoDataService;

    /**
     * POST /api/demo/generate
     * Clears old exam/student data and generates 20 fresh students with detailed grades and attendance.
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generate() {
        try {
            Map<String, Object> result = demoDataService.regenerate();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/demo/clear
     * Clears all exam/student demo data from the database.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clear() {
        try {
            demoDataService.clearDemoData();
            return ResponseEntity.ok(Map.of("message", "✅ All exam & result demo data cleared."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
