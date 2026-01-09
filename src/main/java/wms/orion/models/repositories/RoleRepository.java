package wms.orion.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wms.orion.models.security.Role;

@Repository
public interface RoleRepository extends JpaRepository <Role , Long> {
}
