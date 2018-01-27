package com.beerception.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beerception.entities.Role;

/**
 * JPA Repository for database work over Role object.
 * 
 * @author Miloš Ranđelović
 */
@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Role findByRole(String role);

}