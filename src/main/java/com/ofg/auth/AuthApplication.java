package com.ofg.auth;

import com.ofg.auth.model.entity.Role;
import com.ofg.auth.model.entity.User;
import com.ofg.auth.repository.RoleRepository;
import com.ofg.auth.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication()
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    public CommandLineRunner createUsers(RoleRepository roleRepository,
                                         UserRepository userRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        return roleRepository.save(role);
                    });

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });

            if (userRepository.findByEmail("omer@gmail.com").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(userRole);

                User user = new User();
                user.setEmail("omer@gmail.com");
                user.setPassword(passwordEncoder.encode("P4ssword"));
                user.setFirstName("omer");
                user.setLastName("gulhan");
                user.setPhoneNumber("+905531234567");
                user.setProfileImage("default.png");
                user.setActive(true);
                user.setRoles(roles);
                userRepository.save(user);
            }

            if (userRepository.findByEmail("faruk@gmail.com").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);

                User user = new User();
                user.setEmail("faruk@gmail.com");
                user.setPassword(passwordEncoder.encode("P4ssword"));
                user.setFirstName("faruk");
                user.setLastName("gulhan");
                user.setPhoneNumber("+905531234567");
                user.setProfileImage("default.png");
                user.setActive(true);
                user.setRoles(roles);
                userRepository.save(user);
            }
        };
    }
}