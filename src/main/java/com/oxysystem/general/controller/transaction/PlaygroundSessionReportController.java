package com.oxysystem.general.controller.transaction;

import com.oxysystem.general.service.transaction.PlaygroundSessionReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PlaygroundSessionReportController {
    private final PlaygroundSessionReportService playgroundSessionReportService;

    public PlaygroundSessionReportController(PlaygroundSessionReportService playgroundSessionReportService) {
        this.playgroundSessionReportService = playgroundSessionReportService;
    }

    @GetMapping("/playground-session/report")
    public ResponseEntity<?> getPlaygroundSessionReport(String registrationNumber,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "50") int size) {
        return playgroundSessionReportService.getPlaygroundSessionReport(registrationNumber, page, size);
    }
}
