package translateit2.persistence.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import translateit2.validator.TransuConstraint;

@TransuConstraint
public class TransuDto {
	private long id;

	private long loco; // TODO: => locoId
	
	private int rowId;
	
	@NotEmpty (message = "{TransuDto.target_not_empty}")
	private String targetSegm;

	@NotNull (message = "{TransuDto.source_not_null}")
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
