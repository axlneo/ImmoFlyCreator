package com.codetreatise.service;

import com.codetreatise.bean.Agent;
import com.codetreatise.generic.GenericService;

public interface UserService extends GenericService<Agent> {

	boolean authenticate(String email, String password);
	
	Agent findByEmail(String email);
	
}
