package com.superpet.ProyectoSuperpet.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.superpet.ProyectoSuperpet.service.UsuarioDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
    private UsuarioDetailsService usuarioDetalle;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf
	            .ignoringRequestMatchers("/api/**", "/registro", "/logout") // Desactiva CSRF para APIs y registro y logout
	            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Usa cookies para CSRF
	        )
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/", "/login", "/registro", "/productos/listar","/mascotas/listar", 
	                           "/css/*", "/js/*", "/home", "/menu").permitAll()
	            .requestMatchers("/miperfil").authenticated()
	            .requestMatchers("/mascotas/**","/citas/**").hasRole("CLIENTE")
	            .requestMatchers("/api/productos/**").permitAll()
	            .requestMatchers("/api/mascotas/**").permitAll()
	            .requestMatchers("/api/servicios/**").permitAll()
	            .requestMatchers("/api/clientes/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .defaultSuccessUrl("/menu", true)
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/")
	            .permitAll()
	        );

	    return http.build();
	}
	
	
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(usuarioDetalle)   //ahora funciona :v
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
    }


    //de anterior proyecto 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}