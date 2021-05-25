package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.domain.entety.Sensor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
@ComponentScan("de.throsenheim.vvss21.domain.entety")
public interface SensorRepo extends JpaRepository<Sensor, Integer> {
}
