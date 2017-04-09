package translateit2.lngfileservice;

public class LngFileFormat {
	private String origFilename;
	private String format;
	private String version;
	
	public LngFileFormat(String origFilename, String format, String version) {
		super();
		this.origFilename = origFilename;
		this.format = format;
		this.version = version;
	}

	public LngFileFormat(String origFilename) {
		super();
		this.origFilename = origFilename;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public LngFileFormat(String format, String version) {
		super();
		this.format = format;
		this.version = version;
	}
	
	
}
