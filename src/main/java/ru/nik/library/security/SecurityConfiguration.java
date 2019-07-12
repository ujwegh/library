package ru.nik.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.nik.library.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserService userService;

	private final AuthenticationSuccessHandlerImpl successHandler;

	@Autowired
	public SecurityConfiguration(UserService userService,
		AuthenticationSuccessHandlerImpl successHandler) {
		this.userService = userService;
		this.successHandler = successHandler;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(
				"/registration**",
				"/js/**",
				"/css/**",
				"/img/**",
				"/webjars/**").permitAll()
			.and()
			.authorizeRequests()
				.antMatchers(
					"/books/delete",
					"/books/update/authors",
					"/books/update",
					"/books/update/genres",
					"/books/edit/{\\d+}",
					"/authors/delete",
					"/authors/update",
					"/authors/edit/{\\d+}",
					"/genres/delete",
					"/genres/update",
					"/genres/edit/{\\d+}",
					"/comments/{\\d+}/edit/{\\d+}",
					"/comments/{\\d+}/delete").hasRole("ADMIN")
			.and().authorizeRequests()
			.antMatchers(HttpMethod.POST,
				"/books",
				"/authors",
				"/genres",
				"/rest/authors",
				"/rest/genres",
				"/rest/books",
				"/rest/books/{\\d+}/authors",
				"/rest/books/{\\d+}/genres").hasRole("ADMIN")
			.and().authorizeRequests().antMatchers(
				HttpMethod.DELETE,
				"/rest/authors/{\\d+}",
				"/rest/books/{\\d+}",
				"/rest/comments/{\\d+}/comment/{\\d+}",
				"/rest/genres/{\\d+}").hasRole("ADMIN")
			.and().authorizeRequests()
			.antMatchers(HttpMethod.PUT,
				"/rest/authors/{\\d+}",
				"/rest/genres/{\\d+}",
				"/rest/books/{\\d+}",
				"/rest/comments/{\\d+}").hasRole("ADMIN")
			.anyRequest().fullyAuthenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
			.and()
				.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
			.permitAll()
			.and().httpBasic()
			.and().csrf().disable();

	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userService)
			.passwordEncoder(encoder())
			.and()
			.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
}
