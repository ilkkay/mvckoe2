package translateit2.persistence.dto;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import translateit2.persistence.model.Transu;

public class LocoDto {
	private Long id;
	
	private String name;
	private String projectName;
	private String origFilename;
	private String targetFilename;
	private Locale origLocale;
	private Locale targetLocale;
	
	//TODO: => dto
	private Set<Transu> transus = new HashSet<Transu>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOrigFilename() {
		return origFilename;
	}

	public void setOrigFilename(String origFilename) {
		this.origFilename = origFilename;
	}

	public String getTargetFilename() {
		return targetFilename;
	}

	public void setTargetFilename(String targetFilename) {
		this.targetFilename = targetFilename;
	}

	public Set<Transu> getTransus() {
		return transus;
	}

	public void setTransus(Set<Transu> transus) {
		this.transus = transus;
	}
	
	public Transu getTransuByRowId(long rowId){
		return transus.stream().filter(t->rowId==t.getRowId())
		.findAny()									// If 'findAny' then return found
		.orElse(null);								// If not found, return null
	}

	public Locale getOrigLocale() {
		return origLocale;
	}

	public void setOrigLocale(Locale origLocale) {
		this.origLocale = origLocale;
	}

	public Locale getTargetLocale() {
		return targetLocale;
	}

	public void setTargetLocale(Locale targetLocale) {
		this.targetLocale = targetLocale;
	}
	
	
}