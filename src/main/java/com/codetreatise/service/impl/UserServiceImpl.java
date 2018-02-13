package com.codetreatise.service.impl;

import java.util.List;

import com.codetreatise.bean.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codetreatise.repository.UserRepository;
import com.codetreatise.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private Agent authenticateAgent;

	public Agent getAuthenticateAgent() {
		return authenticateAgent;
	}

	public void setAuthenticateAgent(Agent authenticateAgent) {
		this.authenticateAgent = authenticateAgent;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Agent save(Agent entity) {
		return userRepository.save(entity);
	}

	@Override
	public Agent update(Agent entity) {
		return userRepository.save(entity);
	}

	@Override
	public void delete(Agent entity) {
		userRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		userRepository.delete(id);
	}

	@Override
	public Agent find(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public List<Agent> findAll() {
		return userRepository.findAll();
	}

	@Override
	public boolean authenticate(String username, String password){
		Agent user = this.findByEmail(username);
		if(user == null){
			return false;
		}else{
			if(password.equals(user.getPassword())){
				this.setAuthenticateAgent(user);
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public Agent findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void deleteInBatch(List<Agent> users) {
		userRepository.deleteInBatch(users);
	}
	
}
