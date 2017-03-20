package translateit2.persistence.oldmodel;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

// https://github.com/hellokoding/jpa-onetomany-springboot-maven-mysql/commit/b307db68fa39c3a6fde51f0e73a9b94430ea3ca9

@Entity
@Table
public class Loco implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String projectName;
	private String origFilename;
	private String targetFilename;
	
	@Transient
	private Locale origLocale;
	@Transient
	private Locale targetLocale;
	
	public Loco(){
	}
	
	public Loco(String projectName, LinkedHashSet<Transu> transus){
		this.projectName = projectName;
		this.transus = transus;
	}
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy="loco")
	private Set<Transu> transus = new LinkedHashSet<Transu>();	
	public Set<Transu> getTransus() {
		return transus;	    	
	}
	public void setTransus(LinkedHashSet<Transu> transus) { 
		this.transus = transus; 
	}
	
	
	public Long getId() {
		return id;
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

}