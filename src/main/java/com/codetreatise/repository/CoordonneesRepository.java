package com.codetreatise.repository;

import com.codetreatise.bean.Coordonnees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordonneesRepository extends JpaRepository<Coordonnees, Long> {


}
