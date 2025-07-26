package com.es2.bicicletario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@Service
public class BdService {

    private EntityManager entityManager;

    @Autowired
    public BdService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void resetDatabase() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        Query tableQuery = entityManager.createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'");

        @SuppressWarnings("unchecked")
        List<String> tables = tableQuery.getResultList();

        for (String table : tables) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table + " RESTART IDENTITY").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}