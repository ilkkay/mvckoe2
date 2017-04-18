package translateit2.persistence.model2;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import translateit2.util.LngFileType;

/**  
 *   
 *  version
 */  

@Entity
@Table
@DiscriminatorValue("SOURCE")
public class Tranve extends Work implements Serializable{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Project project;

	private String format;

	private String filename;

	@Enumerated(EnumType.STRING)
	private LngFileType type;
	
	private String fid;
	
	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public LngFileType getType() {
		return type;
	}

	public void setType(LngFileType type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
