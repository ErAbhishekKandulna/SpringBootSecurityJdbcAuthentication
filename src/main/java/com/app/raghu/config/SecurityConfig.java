package com.app.raghu.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	//authorization
	@Bean
	public SecurityFilterChain configureAuth(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(
				request->request.antMatchers("/home","/").permitAll()
				.antMatchers("/admin").hasAuthority("ADMIN")
				.antMatchers("/customer").hasAuthority("CUSTOMER")
				.anyRequest().authenticated()
				)
		.formLogin(form->form.loginPage("/login").permitAll()
				.defaultSuccessUrl("/hello",true)
				)
		.logout(logout->logout.permitAll());
		
		return http.build();
	}
	
	//authentication
	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource)
	{
		UserDetails user1 = User.withUsername("sam").password("$2a$10$JhzMXSO1BUoaRiqNby.6GuAv.U9eqKdteXIHrNufFzEGvIX6Xw99q").authorities("ADMIN").build();
		UserDetails user2 = User.withUsername("ram").password("$2a$10$82YtRVX7P17spNQB55WHaebesNa8g7Jhj8q9iEzUZwFIZkMlEfu8u").authorities("CUSTOMER").build();
		
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.createUser(user1);
		users.createUser(user2);
		return users;
	}
}
