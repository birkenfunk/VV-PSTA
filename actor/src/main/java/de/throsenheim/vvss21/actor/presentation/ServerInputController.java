package de.throsenheim.vvss21.actor.presentation;

import de.throsenheim.vvss21.actor.domain.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/shutter")
public class ServerInputController {

    private Status currentStatus = Status.OPEN;

    @PostMapping("")
    public ResponseEntity<Status> setShutterStatus(@RequestParam Status status){
        currentStatus = status;
        return ResponseEntity.ok(currentStatus);
    }

}
