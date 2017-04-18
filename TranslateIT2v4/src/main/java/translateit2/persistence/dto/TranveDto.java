package translateit2.persistence.dto;

import translateit2.util.LngFileType;

public class TranveDto extends WorkDto{
	private long projectId; 

	private String format;

	private String filename;

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

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}	
}
