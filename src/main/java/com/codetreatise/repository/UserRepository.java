package com.codetreatise.repository;

import com.codetreatise.bean.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codetreatise.bean.User;

@Repository
public interface UserRepository extends JpaRepository<Agent, Long> {

	Agent findByEmail(String email);
}
