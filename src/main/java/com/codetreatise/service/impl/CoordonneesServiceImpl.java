package com.codetreatise.service.impl;

import com.codetreatise.bean.Agent;
import com.codetreatise.bean.Coordonnees;
import com.codetreatise.repository.CoordonneesRepository;
import com.codetreatise.service.CoordonneesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordonneesServiceImpl implements CoordonneesService {




    @Autowired
    private CoordonneesRepository coordonneesRepository;

    @Override
    public Coordonnees save(Coordonnees entity) {
        return coordonneesRepository.save(entity);
    }

    @Override
    public Coordonnees update(Coordonnees entity) {
        return coordonneesRepository.save(entity);
    }

    @Override
    public void delete(Coordonnees entity) {
        coordonneesRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        coordonneesRepository.delete(id);
    }

    @Override
    public Coordonnees find(Long id) {
        return coordonneesRepository.findOne(id);
    }

    @Override
    public List<Coordonnees> findAll() {
        return coordonneesRepository.findAll();
    }



    @Override
    public void deleteInBatch(List<Coordonnees> users) {
        coordonneesRepository.deleteInBatch(users);
    }
}
