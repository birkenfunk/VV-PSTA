package de.throsenheim.vvss21.persistence.repos;

import de.throsenheim.vvss21.persistence.entety.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepo extends JpaRepository<Sensor, Integer> {
}
