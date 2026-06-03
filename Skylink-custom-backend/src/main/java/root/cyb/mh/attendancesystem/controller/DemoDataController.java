package root.cyb.mh.attendancesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.cyb.mh.attendancesystem.service.DemoDataService;

import java.util.Map;

/**
 * Public REST endpoint to trigger demo data generation.
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
     * Clears old ACAD- demo data and regenerates 20 fresh academic members.
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
     * Clears all ACAD- demo data from the database.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clear() {
        try {
            demoDataService.clearDemoData();
            return ResponseEntity.ok(Map.of("message", "✅ All demo data cleared."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
