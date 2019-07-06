package ru.nik.library.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.nik.library.domain.User;
import ru.nik.library.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {

	User save(UserRegistrationDto registration);

	User findByEmail(String email);
}
