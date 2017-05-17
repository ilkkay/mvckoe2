package translateit2.persistence.dto;

import java.util.Locale;

import translateit2.lngfileservice.LngFileFormat;
import translateit2.lngfileservice.LngFileType;

public class ProjectDto {
	private long id;
	
	private long personId;
	
	private long infoId;
	
	private String name;
	
	private LngFileFormat format;

	private LngFileType type;

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
