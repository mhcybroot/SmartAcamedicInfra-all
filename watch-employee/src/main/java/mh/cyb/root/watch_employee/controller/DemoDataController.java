package mh.cyb.root.watch_employee.controller;

import mh.cyb.root.watch_employee.service.DemoDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoDataController {

    private final DemoDataService demoDataService;

    public DemoDataController(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateDemoData() {
        try {
            Map<String, Object> response = demoDataService.regenerate();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearDemoData() {
        try {
            demoDataService.clearDemoData();
            return ResponseEntity.ok(Map.of(
                    "success", "true",
                    "message", "🗑️ Cleared all watch-employee demo data successfully."
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", "false",
                    "error", e.getMessage()
            ));
        }
    }
}
