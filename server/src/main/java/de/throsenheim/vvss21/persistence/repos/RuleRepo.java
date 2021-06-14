package de.throsenheim.vvss21.persistence.repos;

import de.throsenheim.vvss21.persistence.entety.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepo extends JpaRepository<Rule, Integer> {
}
