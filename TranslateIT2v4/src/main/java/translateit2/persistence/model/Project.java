package translateit2.persistence.model;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import translateit2.lngfileservice.LngFileFormat;
import translateit2.lngfileservice.LngFileType;

@Entity (name="trProject")
@Table (name="TR_PROJECT")
public class Project implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Person person;
	
	@ManyToOne
	private Info info;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private LngFileFormat format;

	@Enumerated(EnumType.STRING)
	private LngFileType type;

	private Locale sourceLocale;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
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

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
	
}
