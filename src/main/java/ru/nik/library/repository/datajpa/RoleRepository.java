package ru.nik.library.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nik.library.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
