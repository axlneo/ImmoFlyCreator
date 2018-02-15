package com.pandasoftware.service;

import com.pandasoftware.bean.Agent;
import com.pandasoftware.generic.GenericService;

public interface AgentService extends GenericService<Agent> {

    Agent getAuthenticateAgent();

    boolean authenticate(String email, String password);

    Agent findByEmail(String email);


}
