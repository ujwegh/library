package ru.nik.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Role;
import ru.nik.library.domain.User;
import ru.nik.library.dto.UserDto;
import ru.nik.library.dto.UserRegistrationDto;
import ru.nik.library.repository.datajpa.RoleRepository;
import ru.nik.library.repository.datajpa.UserRepository;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User save(UserRegistrationDto registration) {

		Role role = roleRepository.findByName("ROLE_USER");

		if (role == null) {
			role = new Role(null, "ROLE_USER");
			roleRepository.save(role);
		}

		User user = new User();
		user.setFirstName(registration.getFirstName());
		user.setLastName(registration.getLastName());
		user.setEmail(registration.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
		user.setRoles(Collections.singletonList(role));
		return userRepository.save(user);
	}

	@Override
	public User update(UserDto userDto) {
		List<Role> roleList = new ArrayList<>();
		String[] roles = userDto.getRoles().split(", ");

		List<Role> existedRoles = roleRepository.findAllByNameIn(roles);

		for (String roleName : roles) {
			existedRoles.forEach(role -> roleList.add(roleName.equals(role.getName()) ? role : new Role(null, roleName)));
		}

		User user = userRepository.findByEmail(userDto.getEmail());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setEnabled(userDto.isEnabled());
		user.setPassword(userDto.getPassword());
		user.setRoles(roleList);

		return userRepository.save(user);
	}

	@Override
	public User findByEmail(String email){
		return userRepository.findByEmail(email);
	}

	@HystrixCommand(fallbackMethod = "getDefaultUser")
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(),
			user.getPassword(),
			mapRolesToAuthorities(user.getRoles()));
	}

	public UserDetails getDefaultUser(String email) {
		log.info("Return default user..");
		User user = new User();
		user.setEmail("user");
		user.setFirstName("user");
		user.setLastName("");
		user.setPassword(bCryptPasswordEncoder.encode("password"));
		user.setRoles(Collections.singletonList(new Role("ROLE_DEFAULT")));

		return new org.springframework.security.core.userdetails.User(user.getEmail(),
			user.getPassword(),
			mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream()
			.map(role -> new SimpleGrantedAuthority(role.getName()))
			.collect(Collectors.toList());
	}
}
