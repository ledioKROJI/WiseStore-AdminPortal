package be.ledio.adminportal.repository;

import org.springframework.data.repository.CrudRepository;

import be.ledio.adminportal.model.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByName(String name);
}
