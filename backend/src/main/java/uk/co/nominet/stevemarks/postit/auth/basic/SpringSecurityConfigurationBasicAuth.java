package uk.co.nominet.stevemarks.postit.auth.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfigurationBasicAuth extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()	
		.authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inMemoryUserDetailsManager());
	}

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		final Properties users = new Properties();
		users.put("admin","{noop}password,ROLE_USER,enabled");
		users.put("functional-tester","{noop}password,ROLE_USER,enabled");
		users.put("performance-tester","{noop}password,ROLE_USER,enabled");
		return new InMemoryUserDetailsManager(users);
	}
}
