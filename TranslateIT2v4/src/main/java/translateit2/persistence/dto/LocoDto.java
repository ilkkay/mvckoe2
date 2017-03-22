package translateit2.persistence.dto;

import java.util.HashSet;
import java.util.Set;

import translateit2.persistence.model.Transu;

public class LocoDto extends AbstractLocoDto{
	private Long id;
	
	private String name;
	private String projectName;
	private String origFilename;
	private String targetFilename;
	
	private Set<Transu> transus = new HashSet<Transu>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
	public boolean isNil() {
		// TODO Auto-generated method stub
		return false;
	}
}
