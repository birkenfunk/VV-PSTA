package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.domain.entety.SensorData;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
@ComponentScan("de.throsenheim.vvss21.domain.entety")
public interface SensorDataRepo extends JpaRepository<SensorData, Integer> {
}
