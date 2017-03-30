package translateit2.persistence.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import translateit2.persistence.model.Transu;

@Component
public class LocoMapper extends ModelMapper {	
	
	PropertyMap<Transu, TransuDto> transuMap = new PropertyMap<Transu, TransuDto>() {
		@Override
		protected void configure() {
			map().setLoco(source.getLoco().getId());			
		}
	};

	PropertyMap<TransuDto, Transu> transuMap2 = new PropertyMap<TransuDto, Transu>() {
		@Override
		protected void configure() {
			skip().setLoco(null);
		}
	};
	
	public LocoMapper() {
		addMappings(transuMap);
		addMappings(transuMap2);
	}
	
	
}
