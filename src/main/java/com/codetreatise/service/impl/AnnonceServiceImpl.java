package com.codetreatise.service.impl;

import com.codetreatise.bean.Annonce;
import com.codetreatise.repository.AnnonceRepository;
import com.codetreatise.service.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnonceServiceImpl implements AnnonceService {



    @Autowired
    private AnnonceRepository annonceRepository;

    @Override
    public Annonce save(Annonce entity) {
        return annonceRepository.save(entity);
    }

    @Override
    public Annonce update(Annonce entity) {
        return annonceRepository.save(entity);
    }

    @Override
    public void delete(Annonce entity) {
        annonceRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        annonceRepository.delete(id);
    }

    @Override
    public Annonce find(Long id) {
        return annonceRepository.findOne(id);
    }

    @Override
    public List<Annonce> findAll() {
        return annonceRepository.findAll();
    }

    @Override
    public void deleteInBatch(List<Annonce> annonces) {
        annonceRepository.deleteInBatch(annonces);
    }

}
