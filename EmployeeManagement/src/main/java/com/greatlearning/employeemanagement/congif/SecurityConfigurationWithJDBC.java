package com.greatlearning.employeemanagement.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfigurationWithJDBC extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.jdbcAuthentication().dataSource(dataSource).withDefaultSchema()
				.withUser(
						User.withUsername("sailaja").password(getPasswordEncoder().encode("sailaja")).roles("Admin"))
				.withUser(User.withUsername("varun").password(getPasswordEncoder().encode("varun"))
						.roles("Normal_User"));
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/h2-console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/createService/addNewEmployees", "/deleteService/deleteById",
						"/updateService/updateExistingEmployee")
				.hasRole("Admin")
				.antMatchers("/getService/getAllEmployees", "/readService/getEmployeeById",
					"/readService/findByFirstName", "/readService/sortedByFirstName")
				.hasAnyRole("Admin", "Normal_User")
				.antMatchers("/").permitAll().and().formLogin();
	}

}