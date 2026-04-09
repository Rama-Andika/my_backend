package com.oxysystem.general.controller.general;

import com.oxysystem.general.dto.general.parent.data.ParentDTO;
import com.oxysystem.general.service.general.PlaygroundParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PlaygroundParentController {
    private final PlaygroundParentService playgroundParentService;

    public PlaygroundParentController(PlaygroundParentService playgroundParentService) {
        this.playgroundParentService = playgroundParentService;
    }

    @PostMapping("/parent")
    public ResponseEntity<?> save(@Valid @RequestBody ParentDTO body){
        return playgroundParentService.save(body);
    }
}
