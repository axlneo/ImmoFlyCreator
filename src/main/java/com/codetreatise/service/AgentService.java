package com.codetreatise.service;

import com.codetreatise.bean.Agent;
import com.codetreatise.generic.GenericService;

public interface AgentService extends GenericService<Agent> {

    Agent getAuthenticateAgent();

    boolean authenticate(String email, String password);

    Agent findByEmail(String email);


}
