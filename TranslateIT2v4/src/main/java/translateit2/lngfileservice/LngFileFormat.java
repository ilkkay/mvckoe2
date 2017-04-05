package translateit2.lngfileservice;

public class LngFileFormat {
	private String filename;
	private String format;
	private String version;
	
	public LngFileFormat(String filename, String format, String version) {
		super();
		this.filename = filename;
		this.format = format;
		this.version = version;
	}

	public LngFileFormat(String filename) {
		super();
		this.filename = filename;
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
