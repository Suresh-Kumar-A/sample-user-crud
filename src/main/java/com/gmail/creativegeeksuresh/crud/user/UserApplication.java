package com.gmail.creativegeeksuresh.crud.user;

import com.gmail.creativegeeksuresh.crud.user.model.Role;
import com.gmail.creativegeeksuresh.crud.user.service.RoleService;
import com.gmail.creativegeeksuresh.crud.user.service.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Autowired
	private RoleService roleService;

	@Override
	public void run(String... args) throws Exception {
		try {

			// checking if all role exists otherwise adding them
			if (roleService.getAllRoles().size() == 0) {
				AppConstants.ROLE_SET.forEach(roleName -> {
					try {
						Role role = new Role();
						role.setRoleName(roleName);
						roleService.createRole(role);
					} catch (Exception e) {
						System.err.println(e.getLocalizedMessage());
						e.printStackTrace();
					}
				});
			}

		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}

	}
}
