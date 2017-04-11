package translateit2.persistence.model2;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import translateit2.persistence.model2.Work;

/**
 * a Translation Unit = { (untranslated) source segment, (translated) target segment } 
 */

// http://stackoverflow.com/questions/13687613/jpa-deleting-bidirectional-association-from-inverse-side
@Entity
public class Transu2 implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private Person person;
	
	@ManyToOne	
	@JoinColumn(name="WORK_ID")
	private Work work;

	private int rowId;

	@Column(columnDefinition="TEXT")
	private String segmentId;

	@Column(columnDefinition="TEXT")
	private String segment;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Work getWork() {
		return work;
	}
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	/* 
	 * new transu(this.loco==null) => this.loco = new loco
	 * cur transu (this.loco!=null)=> RemoveTransu
	 * loco <> null => AddTransu
	 * loco == null => this.loco = null => remove transu, if existed
	 */
	public void setWork(Work work) {
		if (this.work != null) { this.work.internalRemoveTransu(this); }
		this.work = work;
		if (work != null) {work.internalAddTransu(this); }
	}

	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}	

}
