package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.domain.entety.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataRepo extends JpaRepository<SensorData, Integer> {
}
