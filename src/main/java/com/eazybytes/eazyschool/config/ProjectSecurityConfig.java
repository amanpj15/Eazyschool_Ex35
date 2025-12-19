package com.eazybytes.eazyschool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

//Since this is a public API/public path, we can ignore the CSRF for any path ie starting with the /public.
        http.csrf((csrf) -> csrf.ignoringRequestMatchers("/saveMsg").ignoringRequestMatchers("/public/**"))
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/dashboard").authenticated()
                    .requestMatchers("/displayMessages").hasRole("ADMIN")
                    .requestMatchers("/closeMsg/**").hasRole("ADMIN")
                    .requestMatchers("/", "/home").permitAll()
                    .requestMatchers("/holidays/**").permitAll()
                    .requestMatchers("/displayProfile").authenticated()
                    .requestMatchers("/updateProfile").authenticated()
                    .requestMatchers("/contact").permitAll()
                    .requestMatchers("/saveMsg").permitAll()
                    .requestMatchers("/courses").permitAll()
                    .requestMatchers("/about").permitAll()
                    .requestMatchers("/assets/**").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/logout").permitAll()
                	.requestMatchers("/public/**").permitAll())
                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
                        .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    
//    So this method, which has @Bean annotation, 
//    will generate a bean of PasswordEncoder.
    @Bean
    public PasswordEncoder passswordEncoder() {
    	return new BCryptPasswordEncoder();
    }
/* 	The reason for returning BCrpty is this will give me 
   	flexibility in future whenever I want to change the 
   	implementation from BCrypt to the other, hashing algo 
   	may be a SCriptPasswordEncoder. */

    
// we have removed this since we are fetching values from DB
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("12345")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("54321")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

}
