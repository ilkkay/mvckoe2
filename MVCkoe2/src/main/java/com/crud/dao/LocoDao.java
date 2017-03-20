package com.crud.dao;

import java.io.Serializable;
import java.util.List;
import com.crud.model.Transu;

public interface LocoDao extends Serializable{
	public long save(Transu t);
	public long update(Transu t);
	public Transu getTransuById(long id);
	public long delete(long id);
	public List<Transu> getTransus();
}
