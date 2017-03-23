package translateit2.persistence.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import translateit2.persistence.model.Transu;

@Component //or @Configuration 
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
			map().setLoco(null);
		}
	};
	
	@Bean
	@Primary
	public LocoMapper locoMap() {
		LocoMapper mp = new LocoMapper();
		mp.addMappings(transuMap);
		mp.addMappings(transuMap2);
		return mp;
	}
	
}
