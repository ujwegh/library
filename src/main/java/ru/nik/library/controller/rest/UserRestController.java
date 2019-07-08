package ru.nik.library.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nik.library.dto.UserDto;
import ru.nik.library.service.UserService;

@RestController
@Slf4j
public class UserRestController {

	private final UserService userService;

	@Autowired
	public UserRestController(UserService userService) {
		this.userService = userService;
	}

	@PutMapping("/rest/user/update")
	public UserDto updateUser(@RequestBody UserDto userDto) {
		log.info("Update user: {}", userDto.toString());
		userDto.setRoles("ROLE_USER");
		userService.update(userDto);
		return userDto;
	}

}
