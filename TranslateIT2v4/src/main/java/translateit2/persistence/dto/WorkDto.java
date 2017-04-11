package translateit2.persistence.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class WorkDto {
	private Long id;

	private String locale;

	private Set<TransuDto> transus = new HashSet<TransuDto>();

	public WorkDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Set<TransuDto> getTransus() {
		return transus;
	}

	public void setTransus(Set<TransuDto> transus) {
		this.transus = transus;
	}

	public TransuDto getTransuByRowId(long rowId){
		return transus.stream().filter(t->rowId==t.getRowId())
				.findAny()									// If 'findAny' then return found
				.orElse(null);								// If not found, return null
	}


}

