package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.domain.entety.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepo extends JpaRepository<Rule, Integer> {
}