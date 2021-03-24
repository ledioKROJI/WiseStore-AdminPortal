package be.ledio.adminportal;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import be.ledio.adminportal.model.User;
import be.ledio.adminportal.model.security.Role;
import be.ledio.adminportal.model.security.UserRole;
import be.ledio.adminportal.service.UserService;
import be.ledio.adminportal.utility.SecurityUtility;

@SpringBootApplication
public class AdminPortalApplication implements CommandLineRunner {

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AdminPortalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstName("James");
		user1.setLastName("Bond");
		user1.setUserName("admin");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		user1.setEmail("admin@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(2);
		role1.setName("ROLE_ADMIN");
		userRoles.add(new UserRole(user1, role1));
		userService.createUser(user1, userRoles);
	}

}
