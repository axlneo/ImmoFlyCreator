package com.pandasoftware.service.impl;

import java.util.List;

import com.pandasoftware.bean.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pandasoftware.repository.AgentRepository;
import com.pandasoftware.service.AgentService;

@Service
public class AgentServiceImpl implements AgentService {


    private Agent authenticateAgent;

    @Override
    public Agent getAuthenticateAgent() {
        return authenticateAgent;
    }

    public void setAuthenticateAgent(Agent authenticateAgent) {
        this.authenticateAgent = authenticateAgent;
    }

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public Agent save(Agent entity) {
        return agentRepository.save(entity);
    }

    @Override
    public Agent update(Agent entity) {
        return agentRepository.save(entity);
    }

    @Override
    public void delete(Agent entity) {
        agentRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        agentRepository.delete(id);
    }

    @Override
    public Agent find(Long id) {
        return agentRepository.findOne(id);
    }

    @Override
    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    @Override
    public boolean authenticate(String username, String password){

        Agent agent = this.findByEmail(username);
        if(agent == null){
            return false;
        }else{
            if(password.equals(agent.getPassword())) {
                this.setAuthenticateAgent(agent);
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public Agent findByEmail(String email) {
        return agentRepository.findByEmail(email);
    }

    @Override
    public void deleteInBatch(List<Agent> users) {
        agentRepository.deleteInBatch(users);
    }

}
