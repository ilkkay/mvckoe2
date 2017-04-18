package translateit2.persistence.dto;

import javax.persistence.Transient;

public class Loco2Dto extends WorkDto{
	private long tranveId; 
	
	private long group_Id; // just a thought for testing
	
	@Transient
	private long versionId;
	
	private String translator;

	public long getVersionId() {
		return versionId;
	}

	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public long getTranveId() {
		return tranveId;
	}

	public void setTranveId(long tranveId) {
		this.tranveId = tranveId;
	}
	
	public long getGroup_id() {
		return group_Id;
	}

	public void setGroup_id(long group_id) {
		this.group_Id = group_id;
	}
	
}
