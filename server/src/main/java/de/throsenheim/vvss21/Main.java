package de.throsenheim.vvss21;

import de.throsenheim.vvss21.application.RuleEngine;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Timer;


@SpringBootApplication(scanBasePackages = {"de.throsenheim.vvss21"})
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    @Autowired
    private RuleEngine ruleEngine;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        LOGGER.info("Hello World");
    }

    @Bean
    public void startTimer(){
        Timer ruleTimer = new Timer();
        ruleTimer.scheduleAtFixedRate(ruleEngine,0, 30000);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("3") String appVersion){
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Server API").version(appVersion));
    }

}
