package com.iatechnology.research_platform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.iatechnology.research_platform.entity.ERole;
import com.iatechnology.research_platform.entity.Role;
import com.iatechnology.research_platform.repository.RoleRepository;

@SpringBootApplication
@EnableMethodSecurity
@EnableAsync
public class ResearchPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResearchPlatformApplication.class, args);
		
	}
	@Bean
	CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.count() == 0) {
				Role userRole = new Role();
				userRole.setName(ERole.ROLE_USER);
				roleRepository.save(userRole);
				
				Role moderatorRole = new Role();
				moderatorRole.setName(ERole.ROLE_MODERATEUR);
				roleRepository.save(moderatorRole);
				
				Role adminRole = new Role();
				adminRole.setName(ERole.ROLE_ADMIN);
				roleRepository.save(adminRole);
			}
		};
	}

}
