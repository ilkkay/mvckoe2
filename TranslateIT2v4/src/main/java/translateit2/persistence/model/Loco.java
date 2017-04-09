package translateit2.persistence.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

// https://github.com/hellokoding/jpa-onetomany-springboot-maven-mysql/commit/b307db68fa39c3a6fde51f0e73a9b94430ea3ca9

@Entity
@Table
public class Loco implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Column(unique=true)
	private String projectName;
	
	@Transient	
	private String origFilename;
	@Transient
	private String format;
	@Transient
	private String version;	
	@Transient
	private String targetFilename;	
	@Transient
	private String origLocale;
	@Transient
	private String targetLocale;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, 
			orphanRemoval = true, mappedBy="loco")
	private Set<Transu> transus = new HashSet<Transu>();	
	
	public Set<Transu> getTransus() {
		///return Collections.unmodifiableSet(transus);
		return (transus);
	}
	
	@SuppressWarnings("unchecked")
	public List<Transu> getTransusSorted() {
		return (List<Transu>) transus.stream().sorted((t1, t2) 
				-> Integer.compare(t1.getRowId(),t2.getRowId()));
	}

	public void setTransus(Set<Transu> transus) {
		this.transus = transus;
	}	

	public void addTransu(Transu t) { t.setLoco(this); }
	public void removeTransu(Transu t) { t.setLoco(null); }

	public void internalAddTransu(Transu t) { transus.add(t); }
	public void internalRemoveTransu(Transu t) { transus.remove(t); }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOrigFilename() {
		return origFilename;
	}

	public void setOrigFilename(String origFilename) {
		this.origFilename = origFilename;
	}

	public String getTargetFilename() {
		return targetFilename;
	}

	public void setTargetFilename(String targetFilename) {
		this.targetFilename = targetFilename;
	}

	public String getOrigLocale() {
		return origLocale;
	}

	public void setOrigLocale(String origLocale) {
		this.origLocale = origLocale;
	}

	public String getTargetLocale() {
		return targetLocale;
	}

	public void setTargetLocale(String targetLocale) {
		this.targetLocale = targetLocale;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Transu getTransuByRowId(long rowId){
		return transus.stream().filter(t->rowId==t.getRowId())
		.findAny()									// If 'findAny' then return found
		.orElse(null);								// If not found, return null
	}
	
	public Transu getTransuBySrcSegment(String segment){
		return transus.stream().filter(t->segment.equals(t.getSourceSegm()))
		.findAny()									// If 'findAny' then return found
		.orElse(null);								// If not found, return null
	}
}