package de.throsenheim.vvss21.persistence.repos;

import de.throsenheim.vvss21.persistence.entety.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataRepo extends JpaRepository<SensorData, Integer> {
}
