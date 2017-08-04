package translateit2.restapi;

import java.util.List;

import translateit2.persistence.dto.WorkDto;

public class Works {
    private List<AvailablePriority> availablePriorities;
    private List<WorkDto> works;

    public List<AvailablePriority> getAvailablePriorities() {
        return availablePriorities;
    }

    public List<WorkDto> getWorks() {
        return works;
    }

    public void setAvailablePriorities(List<AvailablePriority> availablePriorities) {
        this.availablePriorities = availablePriorities;
    }

    public void setWorks(List<WorkDto> works) {
        this.works = works;
    }

}
