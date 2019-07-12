package ru.nik.library.repository.datajpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.nik.library.domain.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

	User findByEmail(String email);
}
