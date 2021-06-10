package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.persistence.entety.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepo extends JpaRepository<Actor, Integer> {
}
