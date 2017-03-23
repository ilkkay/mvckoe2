package translateit2.persistence.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * a Translation Unit = { (untranslated) source segment, (translated) target segment } 
 */

// http://stackoverflow.com/questions/13687613/jpa-deleting-bidirectional-association-from-inverse-side
@Entity
public class Transu implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne	
	@JoinColumn(name="LOCO_ID")
	private Loco loco;
		
	private int rowId;
	
	@Column(columnDefinition="TEXT")
	private String targetSegm;
	@Column(columnDefinition="TEXT")
	private String sourceSegm;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Loco getLoco() {
		return loco;
	}
	/* 
	 * new transu(this.loco==null) => this.loco = new loco
	 * cur transu (this.loco!=null)=> RemoveTransu
	 * loco <> null => AddTransu
	 * loco == null => this.loco = null => remove transu, if existed
	 */
	public void setLoco(Loco loco) {
		if (this.loco != null) { this.loco.internalRemoveTransu(this); }
		this.loco = loco;
		if (loco != null) {loco.internalAddTransu(this); }
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
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}	
	
}
