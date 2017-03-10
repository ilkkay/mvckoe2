package com.crud.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * a Translation Unit = { (untranslated) source segment, (translated) target segment } 
 */
@Entity
public class Transu implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private Loco loco;
	
	private String targetSegm;
	private String sourceSegm;
	private boolean translated;
	private boolean reviewed;
	
	public Transu(){
		
	}
	
	public Transu(int id, Loco loco, String targetSegm, String sourceSegm, boolean translated, boolean reviewed) {
		super();
		this.id = id;
		this.loco = loco;
		this.targetSegm = targetSegm;
		this.sourceSegm = sourceSegm;
		this.translated = translated;
		this.reviewed = reviewed;
	}
	public String getTargetSegm() {
		return targetSegm;
	}
	public void setTargetSegm(String targetSegm) {
		this.targetSegm = targetSegm;
	}
	public String getSourceSegm() {
		return sourceSegm;
	}
	public void setSourceSegm(String sourceSegm) {
		this.sourceSegm = sourceSegm;
	}

	public boolean isTranslated() {
		return translated;
	}
	public void setTranslated(boolean translated) {
		this.translated = translated;
	}
	public boolean isReviewed() {
		return reviewed;
	}
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
}

