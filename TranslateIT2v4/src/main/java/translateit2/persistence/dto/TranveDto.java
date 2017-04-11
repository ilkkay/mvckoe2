package translateit2.persistence.dto;

public class TranveDto extends WorkDto{
	private long project; // TODO: => projectId

	private String format;

	private String filename;

	private String version;	

	private String type;	
	
	private String fid;
	
	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getFormat() {
		return format;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public long getProject() {
		return project;
	}

	public void setProject(long project) {
		this.project = project;
	}	
}
