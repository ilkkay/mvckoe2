package translateit2.persistence.model2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import translateit2.persistence.model2.Transu2;

@Entity
@Inheritance
@DiscriminatorColumn(name = "WORK_TYPE")
public abstract class Work {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, 
			orphanRemoval = true, mappedBy="work")
	private Set<Transu2> transus = new HashSet<Transu2>();	

	private String locale;

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


	public Set<Transu2> getTransus() {
		///return Collections.unmodifiableSet(transus);
		return (transus);
	}

	public void setTransus(Set<Transu2> transus) {
		this.transus = transus;
	}	

	public void addTransu(Transu2 t) { t.setWork(this); }
	public void removeTransu(Transu2 t) { t.setWork(null); }

	public void internalAddTransu(Transu2 t) { transus.add(t); }
	public void internalRemoveTransu(Transu2 t) { transus.remove(t); }

	@SuppressWarnings("unchecked")
	public List<Transu2> getTransusSorted() {
		return (List<Transu2>) transus.stream().sorted((t1, t2) 
				-> Integer.compare(t1.getRowId(),t2.getRowId()));
	}

	public Transu2 getTransuByRowId(long rowId){
		return transus.stream().filter(t->rowId==t.getRowId())
				.findAny()									// If 'findAny' then return found
				.orElse(null);								// If not found, return null
	}
}