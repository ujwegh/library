package ru.nik.library.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nik.library.domain.User;
import ru.nik.library.dto.UserRegistrationDto;
import ru.nik.library.service.UserService;

@Controller
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new UserRegistrationDto());
		return "registration";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
		BindingResult result) {

		User existing = userService.findByEmail(userDto.getEmail());
		if (existing != null){
			result.rejectValue("email", null, "There is already an account registered with that email");
		}

		if (result.hasErrors()){
			return "registration";
		}

		userService.save(userDto);
		return "redirect:/registration?success";
	}

	@GetMapping("/")
	public String root() {
		return "redirect:/books";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/user")
	public String userIndex() {
		return "user/index";
	}


}
