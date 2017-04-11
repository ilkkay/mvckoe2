package translateit2.persistence.model2;

/**
 *  localized object: loco (TYPE=TARGET)
 *  
 *  version id
 *  translatorName
 *  
 */ 
import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
@DiscriminatorValue("TARGET")
public class Loco2 extends Work implements Serializable{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Tranve tranve;

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

	public Tranve getTranve() {
		return tranve;
	}

	public void setTranve(Tranve tranve) {
		this.tranve = tranve;
	}
}
