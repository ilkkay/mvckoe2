package translateit2.persistence.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import translateit2.persistence.model2.Person;

public class TransuDto2 {
	private long id;

	private long work; // TODO: => locoId
	
	private long person; // TODO: => personId
	
	private String segmentId;
	
	private String segment;
	
	@Max(10000)
	private int rowId;
	
	// Sic! Messages have been transfered from ValidationMessages.properties 
	// to Messages.properties file. See main() section.
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getWork() {
		return work;
	}
	public void setWork(long work) {
		this.work = work;
	}
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
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
	
	public long getPerson() {
		return person;
	}
	public void setPerson(long person) {
		this.person = person;
	}
}
