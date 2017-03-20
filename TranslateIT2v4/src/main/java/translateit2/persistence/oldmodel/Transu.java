package translateit2.persistence.oldmodel;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

/**
 * a Translation Unit = { (untranslated) source segment, (translated) target segment } 
 */
@Entity
public class Transu implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@OrderBy ("name ASC")// ordering by primary key is assumed
	private long id;

	@ManyToOne
	@JoinColumn(name="LOCO_ID")
	private Loco loco;
	 
	@Column(columnDefinition="TEXT")
	private String targetSegm;
	@Column(columnDefinition="TEXT")
	private String sourceSegm;
	
	@Transient
	private boolean translated;
	@Transient
	private boolean reviewed;
	
	/*
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy="transu")
	private Set<Comment> comments = new LinkedHashSet<Comment>();	
	public Set<Comment> getComments() {
		return comments;	    	
	}
	public void setComments(LinkedHashSet<Comment> comments) { 
		this.comments = comments; 
	}
	*/
	

	public Transu() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Transu(String t, String s) {
		this.targetSegm=t;
		this.sourceSegm=s;
	}

	public Transu(String t, String s, Loco loco) {
		this.targetSegm=t;
		this.sourceSegm=s;
		this.loco = loco;
		loco.getTransus().add(this);
	}
	   
	public String getTargetSegm() {
		return targetSegm;
	}
	public void setTargetSegm(String targetSegm) {
		this.targetSegm = targetSegm;
	}
	public String getSourceSegm() {
		return sourceSegm;
	}
	public void setSourceSegm(String sourceSegm) {
		this.sourceSegm = sourceSegm;
	}
	
	public boolean isTranslated() {
		return translated;
	}
	public void setTranslated(boolean translated) {
		this.translated = translated;
	}
	public boolean isReviewed() {
		return reviewed;
	}
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id=id;
	}
}
