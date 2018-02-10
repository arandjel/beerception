package com.beerception.configuration;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.beerception.entities.Role;
import com.beerception.repository.RoleRepository;

/**
 * Class for setting up test data.
 * Class implements ApplicationRunner, and therefore will be run with Application start.
 * 
 * @author Miloš Ranđelović
 *
 */
@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void run(ApplicationArguments args) {
    	setupRole("ROLE_USER");
    	setupRole("ROLE_ADMIN");
    }
    
    private void setupRole(String roleName) {
    	Role role = roleRepository.findByRole(roleName);
		
    	if(role == null)
    	{	
    		role = new Role();
    		
			role.setRole(roleName);
			roleRepository.save(role);
    	}
	}

}