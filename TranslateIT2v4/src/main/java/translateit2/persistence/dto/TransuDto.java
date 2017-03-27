package translateit2.persistence.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class TransuDto {
	private long id;

	private long loco; // TODO: => locoId
	
	private int rowId;
	
	@NotEmpty
	private String targetSegm;
	
	@NotNull
	private String sourceSegm;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getLoco() {
		return loco;
	}
	public void setLoco(long loco) {
		this.loco = loco;
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
