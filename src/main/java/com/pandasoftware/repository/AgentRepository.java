package com.pandasoftware.repository;

import com.pandasoftware.bean.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

	Agent findByEmail(String email);

}
