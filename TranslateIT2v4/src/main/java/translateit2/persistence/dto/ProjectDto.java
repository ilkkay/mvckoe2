package translateit2.persistence.dto;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import translateit2.lngfileservice.LngFileFormat;
import translateit2.lngfileservice.LngFileType;
import translateit2.validator.ProjectConstraint;

@ProjectConstraint
public class ProjectDto {
	private long id;
	
	private long personId;
	
	private long infoId;
	
	@NotBlank	//The string is not null and the length is greater than zero
	private String name;

	/*
	 * @NotEmpty The CharSequence, Collection, Map or Array object 
	 * cannot be null and not empty (size > 0).
	 */
	@NotNull
	private LngFileFormat format;

	@NotNull
	private LngFileType type;

	@NotNull
	private Locale sourceLocale;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public long getInfoId() {
		return infoId;
	}

	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LngFileFormat getFormat() {
		return format;
	}

	public void setFormat(LngFileFormat format) {
		this.format = format;
	}

	public LngFileType getType() {
		return type;
	}

	public void setType(LngFileType type) {
		this.type = type;
	}

	public Locale getSourceLocale() {
		return sourceLocale;
	}

	public void setSourceLocale(Locale sourceLocale) {
		this.sourceLocale = sourceLocale;
	}
	
	
}
