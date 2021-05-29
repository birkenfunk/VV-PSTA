package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.domain.entety.Rule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
@ComponentScan("de.throsenheim.vvss21.domain.entety")
public interface RuleRepo extends JpaRepository<Rule, Integer> {
}
