package translateit2.persistence.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import translateit2.validator.TransuConstraint;

@TransuConstraint
public class TransuDto {
	private long id;

	private long loco; // TODO: => locoId
	
	@Max(10000)
	private int rowId;
	
	// Sic! Messages have been transfered from ValidationMessages.properties 
	// to Messages.properties file. See main() section.
	//@NotBlank (message = "{TransuDto.target_not_empty}")
	@NotBlank (message = "{TransuDto.segment_size}")
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
