package com.beerception.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beerception.entities.User;

/**
 * JPA Repository for database work over User object.
 * 
 * @author Miloš Ranđelović
 */
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email);
}