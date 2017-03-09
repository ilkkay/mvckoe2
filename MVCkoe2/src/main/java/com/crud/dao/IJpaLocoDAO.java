package com.crud.dao;

import java.util.List;

import com.crud.model.Transu;

public interface IJpaLocoDAO {	
	void save(Transu t);
	
	Transu update(Transu t);
	
	void delete(Transu t);
	
	void deleteById(long Id);
        
	Transu getTransuById(int id);
	
	List<Transu> findAll();
}
