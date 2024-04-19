package iuh.fit.authjwtservice.repository;

import iuh.fit.authjwtservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
