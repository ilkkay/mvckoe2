package translateit2.persistence.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
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
	private String projectName;
	private String origFilename;
	private String targetFilename;
	
	@Transient
	private Locale origLocale;
	@Transient
	private Locale targetLocale;
	
	/*
	@PreRemove
	public void preRemove(){
	    List<Transu> tempList = new ArrayList(transus);
	    for(Transu t : tempList){
	        t.setLoco(null); 
	    }
	}
	*/
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true, mappedBy="loco")
	private Set<Transu> transus = new HashSet<Transu>();	
	
	public Set<Transu> getTransus() {
		///return Collections.unmodifiableSet(transus);
		return (transus);
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

	public Locale getOrigLocale() {
		return origLocale;
	}

	public void setOrigLocale(Locale origLocale) {
		this.origLocale = origLocale;
	}

	public Locale getTargetLocale() {
		return targetLocale;
	}

	public void setTargetLocale(Locale targetLocale) {
		this.targetLocale = targetLocale;
	}

	public Transu getTransuByRowId(long rowId){
		return transus.stream().filter(t->rowId==t.getId())
		.findAny()									// If 'findAny' then return found
		.orElse(null);								// If not found, return null
	}
	
}