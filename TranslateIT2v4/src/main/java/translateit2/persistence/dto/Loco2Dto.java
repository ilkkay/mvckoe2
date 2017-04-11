package translateit2.persistence.dto;

import javax.persistence.Transient;

public class Loco2Dto extends WorkDto{
	private long tranve; // TODO: => tranveId
	
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

	public long getTranve() {
		return tranve;
	}

	public void setTranve(long tranve) {
		this.tranve = tranve;
	}
	
}
