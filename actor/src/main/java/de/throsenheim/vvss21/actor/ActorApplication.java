package de.throsenheim.vvss21.actor;

import de.throsenheim.vvss21.actor.presentation.RegisterAtServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "de.throsenheim.vvss21")
public class ActorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActorApplication.class, args);
        new RegisterAtServer().sendData();
    }

}

