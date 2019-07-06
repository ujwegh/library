package ru.nik.library.repository.datajpa;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.nik.library.domain.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findByEmail(String email);

	@Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.email = ?#{ principal?.email }")
	@Modifying
	@Transactional
	public void updateLastLogin(@Param("lastLogin") Date lastLogin);
}
