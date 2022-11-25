package com.GrafDigital.SecuCom.SecuCom;

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import com.GrafDigital.SecuCom.SecuCom.Services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecuComAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuComAppApplication.class, args);
	}
	// Encoder le mots de passe;
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	// Au démmarage de l'application
	@Bean // Annotation Bean pour que ça exécute, // Donnons notre methode en paramettre
	CommandLineRunner start(AccountService accountService){
		return  args -> {
			// Ajoutons des Rôles
			accountService.addNewRole(new AppRole(null, "USER"));
			accountService.addNewRole(new AppRole(null, "ADMIN"));

			// Ajouter un Admin par défaut
			accountService.addNewUser(new AppUser(null, "Kadidia SAMAKE", "1234", new ArrayList<>()));

			// Attribuer des roles à l'Admin
			accountService.addRoleToUser("Kadidia SAMAKE", "USER");
			accountService.addRoleToUser("Kadidia SAMAKE", "ADMIN");
		};
	}

}
