package com.oxysystem.general.controller.transaction;

import com.oxysystem.general.service.transaction.PlaygroundSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PlaygroundSessionController {
    private final PlaygroundSessionService playgroundSessionService;

    public PlaygroundSessionController(PlaygroundSessionService playgroundSessionService) {
        this.playgroundSessionService = playgroundSessionService;
    }

    @GetMapping("/playground-live-sessions")
    public ResponseEntity<?> getLiveSession() {
        return  playgroundSessionService.getLiveSession();
    }

    @GetMapping("/playground-end-sessions")
    public ResponseEntity<?> getEndSession(String date,
                                           @RequestParam(required = false, defaultValue = "0")int page,
                                           @RequestParam(required = false, defaultValue = "50")int size) {
        return  playgroundSessionService.getEndSession(date, page, size);
    }

    @PatchMapping("/playground-session/{id}/stop")
    public void stopSession(@PathVariable Long id) {
        playgroundSessionService.stopSession(id);
    }
}
