package translateit2.restapi;

import java.util.HashMap;
import java.util.List;

import translateit2.persistence.dto.ProjectDto;

public class Projects {
    private List<ProjectDto> projects;
    private List<AvailableFormat> availableformats;
    private List<AvailableCharacterSet> availableCharacterSets;
    private HashMap<Long, Integer> projectWorkMap;

    public List<ProjectDto> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDto> projects) {
        this.projects = projects;
    }

    public List<AvailableFormat> getAvailableformats() {
        return availableformats;
    }

    public void setAvailableformats(List<AvailableFormat> availableformats) {
        this.availableformats = availableformats;
    }

    public List<AvailableCharacterSet> getAvailableCharacterSets() {
        return availableCharacterSets;
    }

    public void setAvailableCharacterSets(List<AvailableCharacterSet> availableCharacterSets) {
        this.availableCharacterSets = availableCharacterSets;
    }

    public HashMap<Long, Integer> getProjectWorkMap() {
        return projectWorkMap;
    }

    public void setProjectWorkMap(HashMap<Long, Integer> projectWorkMap) {
        this.projectWorkMap = projectWorkMap;
    }

}
