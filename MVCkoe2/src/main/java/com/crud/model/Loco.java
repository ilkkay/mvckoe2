package com.crud.model;


import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * A localized language object.
 */
@Entity
@Table(name="T_LOCO")
public class Loco implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="LOCO_ID")
	private Collection<Transu> transus = new LinkedHashSet<Transu>();
	
	public Loco(){
	}
	
	public Loco(Long id, String name, Collection<Transu> transus) {
		super();
		this.id = id;
		this.name = name;
		this.transus = transus;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Transu> getTransus() {
		return transus;
	}

	public void setTransus(Collection<Transu> transus) {
		this.transus = transus;
	}

}

