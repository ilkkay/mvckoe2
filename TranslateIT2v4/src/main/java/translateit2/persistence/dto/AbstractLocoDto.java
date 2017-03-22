package translateit2.persistence.dto;

import java.util.HashSet;
import java.util.Set;

import translateit2.persistence.model.Transu;

public abstract class AbstractLocoDto {
	
	private Long id;
	private String name;
	private String projectName;
	private String origFilename;
	private String targetFilename;
	private Set<Transu> transus;
	
	public abstract boolean isNil();
	public abstract Long getId();
	public abstract void setId(Long id);
	public abstract String getName();
	public abstract void setName(String name);
	public abstract String getProjectName();
	public abstract void setProjectName(String projectName);
	public abstract String getOrigFilename();
	public abstract void setOrigFilename(String origFilename);
	public abstract String getTargetFilename();
	public abstract void setTargetFilename(String targetFilename);
	public abstract Set<Transu> getTransus();
	public abstract void setTransus(Set<Transu> transus);
	
	
}
