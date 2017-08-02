package translateit2.persistence.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import translateit2.persistence.model.Project;
import translateit2.persistence.model.Unit;

// converter: http://stackoverflow.com/questions/36717365/how-to-use-modelmapper-to-convert-nested-classes
@Component
@Primary
public class ProjectMapper extends ModelMapper {
    PropertyMap<Project, ProjectDto> projectMap = new PropertyMap<Project, ProjectDto>() {

        @Override
        protected void configure() {
            // TODO Auto-generated method stub
            map().setPersonId(source.getPerson().getId());
            map().setInfoId(source.getInfo().getId());
        }
    };

    PropertyMap<ProjectDto, Project> projectDtoMap = new PropertyMap<ProjectDto, Project>() {
        @Override
        protected void configure() {
            skip().setPerson(null);
            skip().setInfo(null);
        }
    };

    PropertyMap<Unit, UnitDto> unitMap = new PropertyMap<Unit, UnitDto>() {
        @Override
        protected void configure() {
            // TODO Auto-generated method stub
            map().setWorkId(source.getWork().getId());
        }
    };

    PropertyMap<UnitDto, Unit> unitDtoMap = new PropertyMap<UnitDto, Unit>() {
        @Override
        protected void configure() {
            skip().setWork(null);
        }
    };

    public ProjectMapper() {
        addMappings(projectMap);
        addMappings(projectDtoMap);
    }

}
