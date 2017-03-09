package com.crud.dao;

import org.springframework.stereotype.Repository;

import com.crud.model.Transu;
// https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-one-configuration/

@Repository
public class JpaLocoDAO extends AbstractJpaDAO<Transu> implements IJpaLocoDAO {

    public JpaLocoDAO() {
        super();

        setClazz(Transu.class);
    }

    // API

}