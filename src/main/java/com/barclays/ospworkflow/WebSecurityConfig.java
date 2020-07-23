package com.barclays.ospworkflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {


	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	RestClient client;

	Log log = new Log();

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
//				.allowedOrigins("http://localhost:4200/*", "*")
//				.allowedMethods("PUT", "DELETE", "POST", "GET")
//				.allowedHeaders("Content-Type, Authorization")
//				.maxAge(3600);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example

		log.setLogString("Confguring Security rules... :Allow /authenticate to be permitted without authentication. Restrict all rest requests and only permit if authenticated.");
		log.setSender("Client WebSecurityConfig");
		client.Logger(log);

		httpSecurity.cors();
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/authenticate").permitAll().
				// all other requests need to be authenticated
						anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
						exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		log.setLogString("Confguring Security rules... : Adding filter to validate token before every request.");
		log.setSender("Client WebSecurityConfig");
		client.Logger(log);
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}