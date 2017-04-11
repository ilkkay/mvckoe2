package translateit2.persistence.dto;

import javax.persistence.Column;

public class ProjectDto {
	private long id;
	
	private Long person;
	
	private String manager;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	private String projectName;

	public Long getPerson() {
		return person;
	}

	public void setPerson(Long person) {
		this.person = person;
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
