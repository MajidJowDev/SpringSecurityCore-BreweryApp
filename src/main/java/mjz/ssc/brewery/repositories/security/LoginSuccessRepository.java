package mjz.ssc.brewery.repositories.security;

import mjz.ssc.brewery.domain.security.LoginSuccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Integer> {
}
