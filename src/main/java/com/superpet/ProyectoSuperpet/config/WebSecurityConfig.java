package com.superpet.ProyectoSuperpet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
            // 🔴 CSRF OFF para APIs
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )

            // 🔐 AUTORIZACIÓN
            .authorizeHttpRequests(auth -> auth
            		
            		//pa las imagenes 
            		.requestMatchers("/uploads/**").permitAll()
            		// ✅ DESPUÉS (permite acceso sin login)
            		.requestMatchers("/api/**").permitAll()

                // ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // públicas web
                .requestMatchers("/", "/login", "/registro",
                                 "/css/**", "/js/**", "/home", "/menu")
                .permitAll()

                // web protegidas
                .requestMatchers("/chat", "/miperfil").authenticated()
                .requestMatchers("/mascotas/**", "/citas/**").hasRole("CLIENTE")

                .anyRequest().authenticated()
            )

            // 🌐 FORM LOGIN (solo web)
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/menu", true)
                .permitAll()
            )

            // 🚪 LOGOUT (web)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )

            .httpBasic();

        return http.build();
    }


    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(usuarioDetalle)
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}