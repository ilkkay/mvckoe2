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

    PropertyMap<Project, ProjectDto> map13 = new PropertyMap<Project, ProjectDto>() {

        @Override
        protected void configure() {
            // TODO Auto-generated method stub
            map().setPersonId(source.getPerson().getId());
            map().setInfoId(source.getInfo().getId());
        }
    };

    PropertyMap<ProjectDto, Project> map14 = new PropertyMap<ProjectDto, Project>() {
        @Override
        protected void configure() {
            skip().setPerson(null);
            skip().setInfo(null);
        }
    };

    PropertyMap<Unit, UnitDto> map15 = new PropertyMap<Unit, UnitDto>() {

        @Override
        protected void configure() {
            // TODO Auto-generated method stub
            map().setWorkId(source.getWork().getId());
        }
    };

    PropertyMap<UnitDto, Unit> map16 = new PropertyMap<UnitDto, Unit>() {
        @Override
        protected void configure() {
            skip().setWork(null);
        }
    };

    public ProjectMapper() {
        addMappings(map13);
        addMappings(map14);
    }

}
