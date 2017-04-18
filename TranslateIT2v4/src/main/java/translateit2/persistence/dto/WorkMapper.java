package translateit2.persistence.dto;


import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.persistence.model2.Loco2;
import translateit2.persistence.model2.Person;
import translateit2.persistence.model2.Project;
import translateit2.persistence.model2.Transu2;
import translateit2.persistence.model2.Tranve;
import translateit2.persistence.model2.Work;

@Component
@Primary
public class WorkMapper extends ModelMapper {	

	PropertyMap<Transu2, TransuDto2> map = new PropertyMap<Transu2, TransuDto2>() {
		@Override
		protected void configure() {
			map().setWorkId(source.getWork().getId());	
			map().setPersonId(source.getPerson().getId());
		}
	};

	PropertyMap<TransuDto2, Transu2> map2 = new PropertyMap<TransuDto2, Transu2>() {
		@Override
		protected void configure() {
			//skip().setLoco(null);
			Work work = null;
			skip().setWork(work);
			skip().setPerson(null);
		}
	};

	PropertyMap<Work, WorkDto> map3 = new PropertyMap<Work, WorkDto>() {

		@Override
		protected void configure() {
			// TODO Auto-generated method stub
			//this.configure();
		}
	};

	PropertyMap<WorkDto, Work> map4 = new PropertyMap<WorkDto, Work>() {
		@Override
		protected void configure() {
			// TODO Auto-generated method stub
			//this.configure();
		}
	};

	PropertyMap<Loco2, Loco2Dto> map5 = new PropertyMap<Loco2, Loco2Dto>() {

		@Override
		protected void configure() {
			// TODO Auto-generated method stub
			map().setTranveId(source.getTranve().getId());	
		}
	};

	PropertyMap<Loco2Dto, Loco2> map6 = new PropertyMap<Loco2Dto, Loco2>() {
		@Override
		protected void configure() {
			// TODO Auto-generated method stub
			skip().setTranve(null);
		}
	};

	PropertyMap<Tranve, TranveDto> map7 = new PropertyMap<Tranve, TranveDto>() {
		@Override
		protected void configure() {
			map().setProjectId(source.getProject().getId());			
		}
	};

	PropertyMap<TranveDto, Tranve> map8 = new PropertyMap<TranveDto, Tranve>() {
		@Override
		protected void configure() {
			skip().setProject(null);
		}
	};

	PropertyMap<Project, ProjectDto> map9 = new PropertyMap<Project, ProjectDto>() {

		@Override
		protected void configure() {
			// TODO Auto-generated method stub
			map().setPersonId(source.getPerson().getId());
		}
	};

	PropertyMap<ProjectDto, Project> map10 = new PropertyMap<ProjectDto, Project>() {
		@Override
		protected void configure() {
			skip().setPerson(null);
		}
	};

	PropertyMap<Person, PersonDto> map11 = new PropertyMap<Person, PersonDto>() {

		@Override
		protected void configure() {
			// TODO Auto-generated method stub
				
		}
	};

	PropertyMap<PersonDto, Person> map12 = new PropertyMap<PersonDto, Person>() {
		@Override
		protected void configure() {
			// TODO Auto-generated method stub
		}
	};
	
	public WorkMapper() {
		addMappings(map);
		addMappings(map2);
		addMappings(map3);
		addMappings(map4);
		addMappings(map5);
		addMappings(map6);
		addMappings(map7);
		addMappings(map8);
		addMappings(map9);
		addMappings(map10);
		addMappings(map11);
		addMappings(map12);
	}


}
