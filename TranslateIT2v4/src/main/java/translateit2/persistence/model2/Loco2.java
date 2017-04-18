package translateit2.persistence.model2;

/**
 *  localized object: loco (TYPE=TARGET)
 *  
 *  version id
 *  translatorName
 *  
 */ 
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table
@DiscriminatorValue("TARGET")
public class Loco2 extends Work implements Serializable{

	private static final long serialVersionUID = 1L;

	@ManyToOne //(cascade=CascadeType.ALL)
	//@OnDelete(action = OnDeleteAction.CASCADE)
	private Tranve tranve;

	private long group_id; // just a thought for testing
	
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

	public long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	
	
}
