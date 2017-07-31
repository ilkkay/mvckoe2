package translate.it2.version1.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * a Translation Unit = { (untranslated) source segment, (translated) target segment } 
 */
@Entity
public class Transu {
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private Loco loco;
	
	private String targetSegm;
	private String sourceSegm;
	private boolean translated;
	private boolean reviewed;
	
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
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
}

