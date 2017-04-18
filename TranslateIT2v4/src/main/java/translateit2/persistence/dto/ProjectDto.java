package translateit2.persistence.dto;

import javax.persistence.Column;

public class ProjectDto {
	private long id;
	
	private Long personId;
	
	private String manager;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	private String projectName;

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
}
