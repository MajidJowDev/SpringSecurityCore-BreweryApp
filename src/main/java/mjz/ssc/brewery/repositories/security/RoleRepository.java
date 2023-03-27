package mjz.ssc.brewery.repositories.security;

import mjz.ssc.brewery.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
