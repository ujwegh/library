package ru.nik.library.repository.datajpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nik.library.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);

	List<Role> findAllByNameIn(String... names);
}
