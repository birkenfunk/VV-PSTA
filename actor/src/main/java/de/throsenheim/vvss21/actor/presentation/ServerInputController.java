package de.throsenheim.vvss21.actor.presentation;

import de.throsenheim.vvss21.actor.common.CurrentState;
import de.throsenheim.vvss21.actor.domain.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/shutter")
public class ServerInputController {

    private static final Logger LOGGER = LogManager.getLogger(ServerInputController.class);

    @PostMapping("")
    public ResponseEntity<Status> setShutterStatus(@RequestParam Status status){
        CurrentState.setStatus(status);
        LOGGER.info("Status was set to {}", status);
        return ResponseEntity.ok(CurrentState.getStatus());
    }

}
