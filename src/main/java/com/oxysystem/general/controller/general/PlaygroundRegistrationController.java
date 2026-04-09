package com.oxysystem.general.controller.general;

import com.oxysystem.general.service.general.PlaygroundRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PlaygroundRegistrationController {
    private final PlaygroundRegistrationService playgroundRegistrationService;

    public PlaygroundRegistrationController(PlaygroundRegistrationService playgroundRegistrationService) {
        this.playgroundRegistrationService = playgroundRegistrationService;
    }

    @GetMapping("/playground-registrations")
    public ResponseEntity<?> getPlaygroundRegistrations(String number,
                                                        @RequestParam(required = false, defaultValue = "0")int page,
                                                        @RequestParam(required = false, defaultValue = "25")int size){
        return playgroundRegistrationService.getPlaygroundRegistrations(number,page,size);
    }
}
